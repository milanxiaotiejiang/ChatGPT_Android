package com.seabreeze.robot.data.common.bitmap.internal;

import java.util.TreeMap;

/**
 * User: milan
 * Time: 2022/6/28 10:42
 * Des: glide : com.bumptech.glide.load.engine.bitmap_recycle
 * <p>
 */
// Never serialized.
@SuppressWarnings("serial")
class PrettyPrintTreeMap<K, V> extends TreeMap<K, V> {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("( ");
        for (Entry<K, V> entry : entrySet()) {
            sb.append('{').append(entry.getKey()).append(':').append(entry.getValue()).append("}, ");
        }
        if (!isEmpty()) {
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        return sb.append(" )").toString();
    }
}
