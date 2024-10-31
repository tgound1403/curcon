package com.example.curcon.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class UpdateWidgetWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(applicationContext)
        val widgets = manager.getGlanceIds(CurConAppWidget::class.java)
        widgets.forEach { widgetId ->
            CurConAppWidget().update(applicationContext, widgetId)
        }
        return Result.success()
    }
}
