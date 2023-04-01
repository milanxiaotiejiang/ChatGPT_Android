package com.seabreeze.robot.data.websocket

import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.data.websocket.model.json.*
import com.seabreeze.robot.data.websocket.model.json.base.WorkStatus
import com.seabreeze.robot.data.websocket.response.WorkStateInfo
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * User: milan
 * Time: 2021/10/29 14:25
 * Des:
 */
object RosSubjectCenter {

    /**
     * BehaviorSubject 接收到订阅前的最后一条数据和订阅后的所有数据。
     * PublishSubject  接收到订阅之后的所有数据。
     */
    private val logSubject = PublishSubject.create<String>()
    fun String.nextLog() = logSubject.onNext(this)
    fun logSubject(): Observable<String> = logSubject.hide()

    private val receivedSubject = PublishSubject.create<String>()
    fun String.nextReceived() = receivedSubject.onNext(this)
    fun receivedSubject(): Observable<String> = receivedSubject.hide()

    private val errorSubject = PublishSubject.create<BaseThrowable>()
    fun BaseThrowable.nextError() = errorSubject.onNext(this)
    fun errorSubject(): Observable<BaseThrowable> = errorSubject.hide()

    private val receivedOdomSubject = PublishSubject.create<Odom>()
    fun Odom.nextOdom() = receivedOdomSubject.onNext(this)
    fun receivedOdomSubject(): Flowable<Odom> = receivedOdomSubject
        .toFlowable(BackpressureStrategy.LATEST).hide()

    private val receivedMapSubject = PublishSubject.create<RosMapData>()
    fun RosMapData.nextMap() = receivedMapSubject.onNext(this)
    fun receivedMapSubject(): Flowable<RosMapData> = receivedMapSubject
        .toFlowable(BackpressureStrategy.LATEST).hide()

    private val workStatusSubject = PublishSubject.create<WorkStatus>()
    fun WorkStatus.nextWorkStateInfo() = workStatusSubject.onNext(this)
    fun receivedWorkStatusSubject(): Observable<WorkStatus> = workStatusSubject.hide()

    private val electricSubject = PublishSubject.create<Int>()
    fun Int.nextElectric() = electricSubject.onNext(this)
    fun receivedElectricSubject(): Observable<Int> = electricSubject.hide()

    private val waterSubject = PublishSubject.create<Pair<Int, Int>>()
    fun Pair<Int, Int>.nextWater() = waterSubject.onNext(this)
    fun receivedWaterSubject(): Observable<Pair<Int, Int>> = waterSubject.hide()

    private val nextWorkStateInfoSubject = PublishSubject.create<WorkStateInfo>()
    fun WorkStateInfo.nextWorkStateInfo() = nextWorkStateInfoSubject.onNext(this)
    fun receivedWorkStateInfo(): Observable<WorkStateInfo> = nextWorkStateInfoSubject.hide()

    private val nextCurrentExecuteTimeSubject = PublishSubject.create<Long>()
    fun Long.nextCurrentExecuteTime() = nextCurrentExecuteTimeSubject.onNext(this)
    fun receivedCurrentExecuteTime(): Observable<Long> = nextCurrentExecuteTimeSubject.hide()

    private val nextIsChargingSubject = PublishSubject.create<Boolean>()
    fun Boolean.nextIsCharging() = nextIsChargingSubject.onNext(this)
    fun receivedIsCharging(): Observable<Boolean> = nextIsChargingSubject.hide()

    private val errorSubscribeSubject = PublishSubject.create<ErrorSubscribe>()
    fun ErrorSubscribe.nextError() = errorSubscribeSubject.onNext(this)
    fun receivedErrorSubscribeSubject(): Observable<ErrorSubscribe> = errorSubscribeSubject.hide()

    private val noticeSubscribeSubject = PublishSubject.create<NoticeSubscribe>()
    fun NoticeSubscribe.nextNotice() = noticeSubscribeSubject.onNext(this)
    fun receivedNoticeSubscribeSubject(): Observable<NoticeSubscribe> =
        noticeSubscribeSubject.hide()

    private val pointSubject = PublishSubject.create<RosTaskPoint>()
    fun RosTaskPoint.nextPoint() = pointSubject.onNext(this)
    fun pointSubject(): Observable<RosTaskPoint> = pointSubject.hide()

    private val scanLaserSubject = PublishSubject.create<SimpleRosScanLaser>()
    fun SimpleRosScanLaser.nextScanLaser() = scanLaserSubject.onNext(this)
    fun receivedScanLaserSubject(): Observable<SimpleRosScanLaser> = scanLaserSubject.hide()

    private val checkAppV1Subject = PublishSubject.create<RosCheckAppV1>()
    fun RosCheckAppV1.nextCheckApp() = checkAppV1Subject.onNext(this)
    fun receivedCheckAppV1Subject(): Observable<RosCheckAppV1> = checkAppV1Subject.hide()

    private val checkAppV2Subject = PublishSubject.create<RosCheckAppV2>()
    fun RosCheckAppV2.nextCheckApp() = checkAppV2Subject.onNext(this)
    fun receivedCheckAppV2Subject(): Observable<RosCheckAppV2> = checkAppV2Subject.hide()

    private val globalPathSubject = PublishSubject.create<GlobalPath>()
    fun GlobalPath.nextGlobalPath() = globalPathSubject.onNext(this)
    fun receivedGlobalPathSubject(): Observable<GlobalPath> = globalPathSubject.hide()

    private val aromaDiffuserSubject = PublishSubject.create<Boolean>()
    fun Boolean.nextAromaDiffuser() = aromaDiffuserSubject.onNext(this)
    fun receivedAromaDiffuserSubject(): Observable<Boolean> = aromaDiffuserSubject.hide()

    private val materialV1Subject = PublishSubject.create<MaterialStatusSubscribeV1>()
    fun MaterialStatusSubscribeV1.nextMaterialStatus() = materialV1Subject.onNext(this)
    fun receivedMaterialStatusV1Subject(): Observable<MaterialStatusSubscribeV1> =
        materialV1Subject.hide()

}