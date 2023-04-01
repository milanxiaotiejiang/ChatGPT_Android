/*
 * Copyright (C) 2016 MarkZhai (http://zhaiyifan.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seabreeze.robot.base.common.blockcanary;

import android.content.Context;
import android.os.Looper;

public final class BlockCanary {

    private final BlockCanaryInternals mBlockCanaryCore;
    private boolean mMonitorStarted = false;

    private BlockCanary() {
        BlockCanaryInternals.setContext(BlockCanaryContext.get());
        mBlockCanaryCore = BlockCanaryInternals.getInstance();
        mBlockCanaryCore.addBlockInterceptor(BlockCanaryContext.get());
    }

    /**
     * Install {@link BlockCanary}
     *
     * @param context            Application context
     * @param blockCanaryContext BlockCanary context
     * @return {@link BlockCanary}
     */
    public static BlockCanary install(Context context, BlockCanaryContext blockCanaryContext) {
        BlockCanaryContext.init(context, blockCanaryContext);
        return get();
    }

    private static final class SInstanceHolder {
        static final BlockCanary sInstance = new BlockCanary();
    }

    /**
     * Get {@link BlockCanary} singleton.
     *
     * @return {@link BlockCanary} instance
     */
    public static BlockCanary get() {
        return SInstanceHolder.sInstance;
    }

    /**
     * Start monitoring.
     */
    public void start() {
        if (!mMonitorStarted) {
            mMonitorStarted = true;
            Looper.getMainLooper().setMessageLogging(mBlockCanaryCore.monitor);
        }
    }

    /**
     * Stop monitoring.
     */
    public void stop() {
        if (mMonitorStarted) {
            mMonitorStarted = false;
            Looper.getMainLooper().setMessageLogging(null);
            mBlockCanaryCore.stackSampler.stop();
        }
    }

}
