/*
 * Copyright (c) 2016, Peter Abeles. All Rights Reserved.
 *
 * This file is part of DeepBoof
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package deepboof.impl.backward.standard;

import deepboof.DFunction;
import deepboof.backward.DSpatialPadding2D_F64;
import deepboof.forward.ConfigSpatial;
import deepboof.tensors.Tensor_F64;

import java.util.List;

/**
 * @author Peter Abeles
 */
public class TestBackwards_DSpatialWindowBCHW extends ChecksBackwards_DSpatialWindow{

	@Override
	public DFunction<Tensor_F64> create(ConfigSpatial config, DSpatialPadding2D_F64 padding ) {
		return new Helper(config, padding);
	}


	public class Helper extends DSpatialWindowBCHW<Tensor_F64,DSpatialPadding2D_F64> {

		Tensor_F64 dout;

		public Helper(ConfigSpatial configSpatial, DSpatialPadding2D_F64 padding ) {
			super(configSpatial, padding);
		}

		@Override
		protected void forwardsAt_inner(Tensor_F64 input, int batch, int channel,
										int inY, int inX, int outY, int outX) {}

		@Override
		protected void forwardsAt_border(DSpatialPadding2D_F64 padded, int batch,
										 int channel, int padY, int padX, int outY, int outX) {}

		@Override
		public void _forward(Tensor_F64 input, Tensor_F64 output) {}

		@Override
		protected void _backwards(Tensor_F64 input, Tensor_F64 dout, Tensor_F64 gradientInput, List<Tensor_F64> gradientParameters) {
			this.dout = dout;
			backwardsBCHW(input, gradientInput);
		}

		@Override
		protected void backwardsAt_inner(Tensor_F64 input, int batch, int channel, int inY, int inX, int outY, int outX) {
			double sum = 0;
			for (int y = 0; y < HH; y++) {
				for (int x = 0; x < WW; x++) {
					sum += input.get(batch,channel,y+inY,x+inX);
				}
			}

			dpadding.d[dpadding.idx(batch,channel,outY,outX)] = sum*dout.get(batch,channel,outY,outX);
		}

		@Override
		protected void backwardsAt_border(DSpatialPadding2D_F64 padded, int batch, int channel, int padY, int padX, int outY, int outX) {
			double sum = 0;
			for (int y = 0; y < HH; y++) {
				for (int x = 0; x < WW; x++) {
					sum += padded.get(batch,channel,y+padY,x+padX);
				}
			}

			dpadding.d[dpadding.idx(batch,channel,outY,outX)] = sum*dout.get(batch,channel,outY,outX);
		}

		@Override
		public Class<Tensor_F64> getTensorType() {
			return Tensor_F64.class;
		}

		@Override
		public void _setParameters(List<Tensor_F64> parameters) {}
	}

}