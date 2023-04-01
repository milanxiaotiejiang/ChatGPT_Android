package com.seabreeze.robot.data

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/1/13
</pre> *
 */
enum class ProtocolEnum(  //个人保护协议
    val introduce: String,
    val title: String
) {
    USER_AGREEMENT("USER_AGREEMENT", "用户协议"),  //用户协议
    USER_PRIVACY_AGREEMENT("USER_PRIVACY_AGREEMENT", "用户隐私协议");  //用户隐私协议;


    companion object {

        fun findEnumByCode(introduce: String): ProtocolEnum {
            values().forEach {
                if (it.introduce == introduce) {
                    //如果需要直接返回name则更改返回类型为String,return statusEnum.name;
                    return it
                }
            }
            throw IllegalArgumentException("code is invalid")
        }
    }

}