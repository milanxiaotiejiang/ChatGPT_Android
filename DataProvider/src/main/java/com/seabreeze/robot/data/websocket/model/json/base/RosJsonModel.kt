package com.seabreeze.robot.data.websocket.model.json.base

import android.graphics.PointF
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.seabreeze.robot.base.ext.foundation.UUIDGenerator
import kotlinx.parcelize.Parcelize

/**
 * User: milan
 * Time: 2021/11/16 10:04
 * Des:
 */
object RosJsonModel {

    const val TRY_TO_ENTER = "try_to_enter"
    const val FORCED_TO_ENTER = "forced_to_enter"
    const val WORK_TO_ENTER = "work_to_enter"
    const val WORK_TO_MAP_APP = "work_to_map_app"
    const val IS_IN_BASEMENT = "is_in_basement"

    const val SAVE_MAP = "save_map"
    const val GET_MULTI_MAPS = "get_multi_maps"
    const val CHANGE_MAP = "change_map"
    const val EDIT_MAP = "edit_map"
    const val GET_EDIT_MAP = "get_edit_map"
    const val ERASER_MAP = "eraser_map"

    const val FULL_COVERAGE_PATH = "full_coverage_path"
    const val GET_FULL_PLAN = "get_full_plan"
    const val SET_FULL_CLEANING_MODE = "set_full_cleaning_mode"
    const val GET_FULL_CLEANING_MODE = "get_full_cleaning_mode"
    const val GET_REGION_FULL_PLAN = "get_region_full_plan"

    const val GET_ROS_VERSION = "get_ros_version"

    const val APP_ALONG_CLEAN = "app_along_clean"
    const val EXECUTE_TASK = "execute_task"
    const val DELETE_TASK = "delete_task"
    const val GET_TASK_LIST = "get_task_list"
    const val GET_FINISHED_POINT = "get_finished_point"
    const val ROBOT_RELOCATION = "robot_relocation"
    const val ROBOT_MOVE = "robot_move"

    const val APP_SPOT = "app_spot"
    const val APP_PAUSE = "app_pause"
    const val APP_CHARGE = "app_charge"

    const val GET_DEVICE_STATUS = "get_device_status"
    const val CHANGE_WORK_STATUS = "change_work_status"
    const val CHANGE_AROMATHERAPY_STATE = "change_aromatherapy_state"

    const val TEACH_MODE_START = "teach_mode_start"
    const val TEACH_MODE_STOP = "teach_mode_stop"
    const val TEACH_HEART_BEAT = "teach_heart_beat"
    const val GET_TEACH_PATH_LIST = "get_teach_path_list"
    const val GET_TEACH_PATH_DETAIL = "get_teach_path_detail"
    const val DELETE_TEACH_PATH_LIST = "delete_teach_path_list"

    const val OPEN_MACHINE_DRAWER = "open_machine_drawer"
    const val OPEN_SELF_CLEANING = "open_self_cleaning"

    const val PLAYER_RECRUIT_VOICE = "player_recruit_voice"

    const val LIGHT_BELT_MODE = "light_belt_mode"

    const val CLEAN_HISTORY_LIST = "clean_history_list"

    const val COMBINATION_PART_ADD = "combination_part_add"
    const val COMBINATION_PART_UPDATE = "combination_part_update"
    const val COMBINATION_PART_DELETE = "combination_part_delete"
    const val COMBINATION_PART_DELETE_FORCE = "combination_part_delete_force"
    const val COMBINATION_PART_LIST = "combination_part_list"
    const val COMBINATION_COMBINATION_ADD = "combination_combination_add"
    const val COMBINATION_COMBINATION_UPDATE = "combination_combination_update"
    const val COMBINATION_COMBINATION_DELETE = "combination_combination_delete"
    const val COMBINATION_COMBINATION_LIST = "combination_combination_list"
    const val COMBINATION_COMBINATION_DETAILS = "combination_combination_details"

    const val GET_MACHINE_MODEL = "get_machine_model"
    const val MAIN_COMBINATION_WAY = "main_combination_way"
    const val CANCEL_MAIN_COMBINATION = "cancel_main_combination"

    const val GET_TIMER_LIST = "get_timer_list"
    const val SET_TIMER = "set_timer"
    const val UPDATE_TIMER = "update_timer"
    const val DELETE_TIMER = "delete_timer"

    const val SAVE_LOCATION = "save_location"
    const val SAVE_PROJECT = "save_project"

    const val PAD_VERSION_INTO = "pad_version_into"

    const val POWER_REDUCTION_1 = "power_reduction_1"

    const val SPLIT_MAP_DATA = "split_map_data"
    const val REGION_SEGMENTATION = "region_segmentation"
    const val REGION_RENAME = "region_rename"
    const val REGION_RESET = "region_reset"

    const val ROOM_MAP_DATA = "room_map_data"
    const val ROOM_MERGE = "room_merge"
    const val ROOM_SEGMENTATION = "room_segmentation"
    const val ROOM_RESET = "room_reset"
    const val ROOM_RENAME = "room_rename"
    const val ROOM_AUTO = "room_auto"
    const val REGION_EXPLORATION = "region_exploration"

    const val GET_MATERIAL_STATUS = "get_material_status"

    const val NOTICE_LIST = "notice_list"

    const val GET_PLAN_PARAM = "get_plan_param"
    const val SET_PLAN_PARAM = "set_plan_param"
    const val RESET_PLAN_PARAM = "reset_plan_param"

    const val SET_EXPLORER_ENERGY = "set_explorer_energy"
    const val GET_EXPLORER_ENERGY ="get_explorer_energy"

    const val RECTANGLE_TYPE = 1
    const val LINE_TYPE = 2
    const val ERASER_TYPE = 9

    const val ALONG_CLEAN_MODE = 1
    const val ZONED_CLEAN_MODE = 2
    const val ONE_KEY_CLEAN_MODE = 6
    const val COMBINATION_CLEAN_MODE = 7
    const val REGION_FULL_MODE = 10

    const val PART_ZONED_MODE = 1
    const val PART_PATH_MODE = 3

    const val ROS_MODE_ESTABLISH = 0
//    const val ROS_MODE_MODIFY = 1

    const val ROS_NOT_LOADED = -1
    const val ROS_SHUT = 0
    const val ROS_OPEN = 1

    const val ROS_GEAR_LITE = 1
    const val ROS_GEAR_STANDARD = 2
    const val ROS_GEAR_POWER = 3

    const val PLAN_FULL = -1
    const val PLAN_ALL_REGION = 0

    const val COMBINATION_TYPE = 0
    const val FULL_CLEANING_TYPE = 1
}

data class RosRequest<T>(
    val method: String,
    val params: T,
    var id: Long = 0,
)

fun rosRequestStr(method: String) = RosRequest(method, "")

fun rosRequestStr(method: String, params: Int) = RosRequest(method, params)

data class RosResponse<T>(
    var result: Boolean = true,
    val id: Long,
    val params: T? = null,
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("error_message")
    val errorMessage: String,
)

data class RosDeviceStatus(
    @SerializedName("battery")
    val battery: Int = 0,//电量
    @SerializedName("clean_area")
    val cleanArea: Long = 0L,//总清洁区域面积
    @SerializedName("clean_time")
    val cleanTime: Long = 0L,//总时间
    @SerializedName("clean_count")
    val cleanCount: Int = 0,//总次数

    @SerializedName("current_map")
    val currentMap: RosMap,//当前加载的地图id

    @SerializedName("is_urgency_stop")
    val isUrgencyStop: Boolean,//是否处于急停

//    @SerializedName("in_cleaning")
//    val inCleaning: Boolean = false,//是否在清扫中
//    @SerializedName("in_returning")
//    val inReturning: Boolean = false,//是否正在基站
    @SerializedName("lock_status")
    val lockStatus: Boolean = false,//抽屉是否开启
    @SerializedName("dust_box_status")
    val dustBoxStatus: Boolean = false,//尘箱是否装载
    @SerializedName("water_box_status")
    val waterBoxStatus: Boolean = false,//水箱是否装载

    @SerializedName("work_status")
    val workStatus: WorkStatus,//当前状态
    @SerializedName("work_status_message")
    val workStatusMessage: String = "",
    @SerializedName("work_status_code")
    val workStatusCode: Int = 10001
)

@Parcelize
data class WorkStatus(
    @SerializedName("sweep_status")
    val sweepStatus: Int,//当前扫头状态
    @SerializedName("mop_status")
    val mopStatus: Int,//当前拖头状态
    @SerializedName("vacuum_status")
    val vacuumStatus: Int,//当前尘吸状态
    @SerializedName("push_status")
    val pushStatus: Int,//当前推头状态
    @SerializedName("aromatherapy_status")
    val aromatherapyStatus: Int,//当前香薰状态 0关闭 1清淡 2标准 3浓郁
    @SerializedName("disinfect_status")
    val disinfectStatus: Int,//当前消杀状态
) : Parcelable {
    override fun toString(): String {
        return "WorkStatus(sweepStatus=$sweepStatus, mopStatus=$mopStatus, vacuumStatus=$vacuumStatus, pushStatus=$pushStatus, aromatherapyStatus=$aromatherapyStatus, disinfectStatus=$disinfectStatus)"
    }
}

data class CleanPointTask(
    @SerializedName("task_id")
    val id: String = UUIDGenerator.uuid,

    @SerializedName("work_status")
    val workStatus: WorkStatus,
)

data class RosTask(
    @SerializedName("task_id")
    var id: String = UUIDGenerator.uuid,

    @SerializedName("mode")
    val mode: Int,

    @SerializedName("work_status")
    val workStatus: WorkStatus,//当前状态

    @SerializedName("rate")
    var rate: Int = 1,//次数
    @SerializedName("time_mode")
    val timeMode: Any? = "",//时间
    @SerializedName("launch_people")
    val launchPeople: String = "App",//发起人
    @SerializedName("launch_time")
    val launchTime: Long = System.currentTimeMillis() / 1000,//发起时间

    @SerializedName("zoned")
    val zoned: Array<Float>,//位置
    @SerializedName("continuity")
    val continuity: RosContinuityTask,
    @SerializedName("polygon")
    val polygon: RosPolygonTask = RosPolygonTask(polygonList = mutableListOf()),
    @SerializedName("teach_path")
    var teachPath: RosTeachPathTask,
    @SerializedName("combination")
    var combination: RosCombinationTask,
    @SerializedName("full_path")
    var fullPath: RosFullPathTask?,

    @SerializedName("in_execute")
    val inExecute: Boolean = false,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RosTask

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

@Parcelize
data class ViewPart(
    @SerializedName("part_id")
    var partId: String = UUIDGenerator.uuid,
    @SerializedName("name")
    val name: String,

    @SerializedName("mode")
    val mode: Int,

    @SerializedName("work_status")
    val workStatus: WorkStatus,//当前状态

    @SerializedName("rate")
    var rate: Int = 1,//次数

    @SerializedName("zoned")
    var zoned: List<PointF>,
    @SerializedName("polygon")
    var polygon: List<PointF> = mutableListOf(),
    @SerializedName("path_id")
    val teachId: String = "",
    @SerializedName("teach")
    val teach: List<PointF> = mutableListOf(),
) : Parcelable

@Parcelize
data class CombinationTask(
    @SerializedName("combination_id")
    var combinationId: String = UUIDGenerator.uuid,
    @SerializedName("name")
    var name: String,
    @SerializedName("rate")
    var rate: Int = 1,
    @SerializedName("view_part_list")
    var viewPartList: List<ViewPart>,
    @SerializedName("principal")
    var principal: Boolean = false,
    @SerializedName("work_status")
    var workStatus: WorkStatus,
    @SerializedName("combination_type")
    var combinationType: Int
) : Parcelable

@Parcelize
data class Cron(
    @SerializedName("timer_id")
    var timerId: String = UUIDGenerator.uuid,
    @SerializedName("timer_rule")
    var timerRule: String,//executionTime
    @SerializedName("timer_name")
    var timerName: String,//displayName
    @SerializedName("task_id")
    var taskId: String,//executionId
    @SerializedName("task_name")
    var taskName: String,//executionName
    @SerializedName("is_execute")
    var isExecute: Boolean,
    @SerializedName("rate")
    var rate: Int,//executionFrequency
    @SerializedName("is_never")
    var isNever: Boolean,
    @SerializedName("is_skip")
    var isSkip: Boolean,
    var year: Int,
    var month: Int,
    var day: Int,
) : Parcelable

data class RosVersion(
    @SerializedName("air_code_version")
    val airCode: String,
    @SerializedName("ds_hard_version")
    val dsH: String,
    @SerializedName("ds_soft_version")
    val dsS: String,
    @SerializedName("app_pad_version")
    val pad: String
)

data class PlanConfigs(
    @SerializedName("robot_radius")
    val robotRadius: Double,//机器人半径 0.1-0.3
    @SerializedName("map_correction_closing_neighborhood_size")
    val mapCorrectionClosingNeighborhoodSize: Int,//外围区域闭合邻域大小 0-5
    @SerializedName("grid_obstacle_offset")
    val gridObstacleOffset: Double,//障碍物的额外偏移 0-0.3
    @SerializedName("path_eps")
    val pathEps: Double,//路径规划时两点间距  1-10
    @SerializedName("min_cell_area")
    val minCellArea: Double,//最小规划面积 30-500
    @SerializedName("max_deviation_from_track")
    val maxDeviationFromTrack: Double,//轨道最大允许偏移量 -1-5
    @SerializedName("range_near_base_station")
    val rangeNearBaseStation: Int,//基站范围 0-10
    @SerializedName("room_area_factor_lower_limit")
    val roomAreaFactorLowerLimit: Double,//临界线分隔的区域允许具有的最小面积 0.1-20
    @SerializedName("room_area_factor_upper_limit")
    val roomAreaFactorUpperLimit: Double,//临界线分隔的区域允许具有的最大面积 100-1000000
    @SerializedName("neighborhood_index")
    val neighborhoodIndex: Int,//搜索临界点 70-600
    @SerializedName("max_iterations")
    val maxIterations: Int,//搜索邻域的最大迭代次数 60-240
    @SerializedName("min_critical_point_distance_factor")
    val minCriticalPointDistanceFactor: Double,//消除临界点与之前两个临界点之间的最小距离 0-1.3
    @SerializedName("max_area_for_merging")
    val maxAreaForMerging: Double,//与其周围房间合并的房间的最大面积 3-1000
    @SerializedName("distance_from_obstacles")
    val distanceFromObstacles: Int,//与障碍物的间距 0-10
    @SerializedName("number_extension")
    val numberExtension: Int,//生成贴边轮廓的个数 1-3
    @SerializedName("multiple_contour_spacing")
    val multipleContourSpacing: Int,//多个贴边轮廓的间距 -3-3
    @SerializedName("random_number_generation_ratio")
    val randomNumberGenerationRatio: Int,//可达点的计算比例 50-200
    @SerializedName("boundary_min_area")
    val boundaryMinArea: Int,//贴边范围的最小面积 0-10
)

object BeanFactory {
    private fun simpleRosContinuityTask(): RosContinuityTask {
        return RosContinuityTask("", false, mutableListOf())
    }

    private fun simpleRosTeachPathTask(): RosTeachPathTask {
        return RosTeachPathTask("")
    }

    private fun simpleRosCombinationTask(): RosCombinationTask {
        return RosCombinationTask("", "")
    }

    private fun simpleRosFullPathTask(): RosFullPathTask {
        return RosFullPathTask(mutableListOf())
    }

    fun generateAlongClean(
        workStatus: WorkStatus,
        taskId: String,
        taskName: String,
        countRate: Int
    ): RosTask {
        return RosTask(
            mode = RosJsonModel.ONE_KEY_CLEAN_MODE,
            workStatus = workStatus,
            zoned = arrayOf(),
            continuity = simpleRosContinuityTask(),
            teachPath = simpleRosTeachPathTask(),
            combination = RosCombinationTask(taskId, taskName),
            fullPath = simpleRosFullPathTask(),
            rate = countRate
        )
    }

    fun generateRegionFullClean(
        identification: String,
        workStatus: WorkStatus,
        countRate: Int
    ): RosTask {
        return RosTask(
            id = identification,
            mode = RosJsonModel.REGION_FULL_MODE,
            workStatus = workStatus,
            zoned = arrayOf(),
            continuity = simpleRosContinuityTask(),
            teachPath = simpleRosTeachPathTask(),
            combination = simpleRosCombinationTask(),
            fullPath = simpleRosFullPathTask(),
            rate = countRate
        )
    }

    fun generateZonedClean(
        identification: String,
        workStatus: WorkStatus,
        zoned: Array<Float>,
        rate: Int = 1,
    ): RosTask {
        return RosTask(
            id = identification,
            mode = RosJsonModel.ZONED_CLEAN_MODE,
            rate = rate,
            workStatus = workStatus,
            zoned = zoned,
            continuity = simpleRosContinuityTask(),
            teachPath = simpleRosTeachPathTask(),
            combination = simpleRosCombinationTask(),
            fullPath = simpleRosFullPathTask()
        )
    }

    fun generateCombinationClean(
        workStatus: WorkStatus,
        combinationId: String,
        combinationName: String,
        count: Int
    ): RosTask {
        return RosTask(
            mode = RosJsonModel.COMBINATION_CLEAN_MODE,
            rate = count,
            workStatus = workStatus,
            zoned = arrayOf(),
            continuity = simpleRosContinuityTask(),
            teachPath = simpleRosTeachPathTask(),
            combination = RosCombinationTask(combinationId, combinationName),
            fullPath = simpleRosFullPathTask()
        )
    }

    fun createWorkStatus(
        sweepStatus: Int = -1,//当前扫头状态
        mopStatus: Int = -1,//当前拖头状态
        vacuumStatus: Int = -1,//当前尘吸状态
        pushStatus: Int = -1,//当前推头状态
        aromatherapyStatus: Int = -1,//当前香薰状态
        disinfectStatus: Int = -1,//当前消杀状态
    ) = WorkStatus(
        sweepStatus, mopStatus, vacuumStatus, pushStatus, aromatherapyStatus, disinfectStatus
    )
}
