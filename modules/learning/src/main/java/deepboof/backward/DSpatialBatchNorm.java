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

package deepboof.backward;

import deepboof.DFunction;
import deepboof.Tensor;
import deepboof.forward.SpatialBatchNorm;

/**
 * <p>Implementation of {@link SpatialBatchNorm Spatial Batch Normalization} for training networks.</p>
 *
 * @author Peter Abeles
 */
public interface DSpatialBatchNorm<T extends Tensor<T>> extends SpatialBatchNorm<T>, DFunction<T> {

    /**
     * Returns learning rate for computing the moving average of the training sets mean and standard
     * deviation.  Higher numbers for faster learning.
     *
     * @return Learning rate. 0 to 1.
     */
    double getLearningRate();
}