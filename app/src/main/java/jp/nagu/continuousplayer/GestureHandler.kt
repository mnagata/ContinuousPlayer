package jp.nagu.continuousplayer

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

/**
 * プレーヤー画面のタッチジェスチャーを処理するハンドラ。
 *
 * - **シングルタップ**: 再生/一時停止の切り替え
 * - **ダブルタップ**: 画面右半分で15秒早送り、左半分で10秒巻き戻し
 * - **左右フリング**: 前/次の動画へスキップ
 *
 * @param view ジェスチャーを受け取るオーバーレイView（ダブルタップ判定の中心位置に使用）
 * @param controller 再生操作を委譲する [PlayerController]
 * @param onPlayPauseToggled 再生/一時停止が切り替わった時のコールバック
 */
class GestureHandler(
    private val view: View,
    private val controller: PlayerController,
    private val onPlayPauseToggled: () -> Unit = {}
) : GestureDetector.SimpleOnGestureListener() {

    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100

    override fun onDown(e: MotionEvent): Boolean = true

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        controller.togglePlayPause()
        onPlayPauseToggled()
        return true
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        val midX = view.width / 2f
        if (e.x > midX) {
            controller.seekForward(15_000L)
        } else {
            controller.seekBackward(10_000L)
        }
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean = true

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d(TAG, "onFling called: e1=$e1, velocityX=$velocityX, velocityY=$velocityY")
        val start = e1 ?: run {
            Log.d(TAG, "onFling: e1 is null")
            return false
        }
        val dx = e2.x - start.x
        val dy = e2.y - start.y
        Log.d(TAG, "onFling: dx=$dx, dy=$dy, absDx=${abs(dx)}, absVelX=${abs(velocityX)}")
        if (abs(dx) > abs(dy) && abs(dx) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
            if (dx > 0) {
                Log.d(TAG, "onFling: previousVideo")
                controller.previousVideo()
            } else {
                Log.d(TAG, "onFling: nextVideo")
                controller.nextVideo()
            }
            return true
        }
        Log.d(TAG, "onFling: thresholds not met")
        return false
    }

    companion object {
        private const val TAG = "GestureHandler"
    }
}
