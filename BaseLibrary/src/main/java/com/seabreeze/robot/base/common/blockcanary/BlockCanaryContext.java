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

import static com.seabreeze.robot.base.ext.tool.ChannelReaderKt.isProdChannel;

import android.content.Context;

import androidx.annotation.NonNull;

import com.seabreeze.robot.base.common.blockcanary.internal.BlockInfo;

/**
 * User should provide a real implementation of this class to use BlockCanary.
 */
public class BlockCanaryContext implements BlockInterceptor {

    private static Context sApplicationContext;
    private static BlockCanaryContext sInstance = null;

    public BlockCanaryContext() {
    }

    static void init(Context context, BlockCanaryContext blockCanaryContext) {
        sApplicationContext = context;
        sInstance = blockCanaryContext;
    }

    public static BlockCanaryContext get() {
        if (sInstance == null) {
            throw new RuntimeException("BlockCanaryContext null");
        } else {
            return sInstance;
        }
    }

    public Context provideContext() {
        return sApplicationContext;
    }

    public String provideQualifier() {
        return "unknown";
    }

    public String provideUid() {
        return "uid";
    }

    public String provideNetworkType() {
        return "unknown";
    }

    public int provideBlockThreshold() {
        return 1000;
    }

    public int provideDumpInterval() {
        return provideBlockThreshold();
    }

    public String providePath() {
        return sApplicationContext.getExternalCacheDir().getPath() + "/blockcanary/";
    }

    /**
     * Block interceptor, developer may provide their own actions.
     */
    @Override
    public void onBlock(@NonNull Context context, @NonNull BlockInfo blockInfo) {
//        Logger.w(blockInfo.getBasicString());
//        Logger.w(blockInfo.getTimeString());
////        Logger.e(blockInfo.getCpuString());
//        Logger.w(blockInfo.getStackString());
    }

    /**
     * Whether to stop monitoring when in debug mode.
     *
     * @return true if stop, false otherwise
     */
    public boolean stopWhenDebugging() {
        return isProdChannel(provideContext());
    }
}