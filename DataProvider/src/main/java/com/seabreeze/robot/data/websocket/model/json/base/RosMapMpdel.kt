package com.seabreeze.robot.data.websocket.model.json.base

import com.google.gson.annotations.SerializedName

/**
 * User: milan
 * Time: 2022/1/24 12:56
 * Des:
 */

data class RosMap(
    @SerializedName("map_name")
    val name: String,
    @SerializedName("map_id")
    val id: Int = -1,
)


data class Location(
    val locationType: Int, //获取当前定位结果来源，如网络定位结果，详见定位类型表
    val latitude: Double,//获取纬度
    val longitude: Double,//获取经度
    val accuracy: Float,//获取精度信息
    val altitude: Double,//海拔高度

    val address: String,//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
    val country: String,//国家信息
    val province: String,//省信息
    val city: String,//城市信息
    val district: String,//城区信息
    val street: String,//街道信息
    val streetNum: String,//街道门牌号信息
    val cityCode: String,//城市编码
    val adCode: String,//地区编码
    val aoiName: String,//获取当前定位点的AOI信息
) {
    override fun toString(): String {
        return "Location(locationType=$locationType, latitude=$latitude, longitude=$longitude, accuracy=$accuracy, address='$address', country='$country', province='$province', city='$city', district='$district', street='$street', streetNum='$streetNum', cityCode='$cityCode', adCode='$adCode', aoiName='$aoiName')"
    }
}

data class Project(
    val name: String
)

data class RegionName (
    @SerializedName("name")
    val regionName: String,
    @SerializedName("id")
    val regionId: Int
)