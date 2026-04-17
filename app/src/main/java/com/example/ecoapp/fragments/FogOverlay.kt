package com.example.ecoapp.fragments

import android.graphics.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import kotlin.math.cos

object FogOverlay : Overlay() {

    private val revealedAreas = mutableListOf<GeoPoint>()

    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        if (shadow) return

        val paint = Paint()
        paint.color = Color.argb(180, 0, 0, 0)

        // draw fog
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

        // CUT holes using clipPath (THIS ALWAYS WORKS)
        val path = Path()

        for (geo in revealedAreas) {

            val pt = mapView.projection.toPixels(geo, null)
            val radius = metersToPixels(mapView, geo, 60.0).toFloat()

            path.addCircle(pt.x.toFloat(), pt.y.toFloat(), radius, Path.Direction.CCW)
        }

        // remove fog where circles are
        canvas.save()
        canvas.clipPath(path, Region.Op.DIFFERENCE)

        canvas.drawRect(
            0f,
            0f,
            canvas.width.toFloat(),
            canvas.height.toFloat(),
            Paint().apply { color = Color.TRANSPARENT }
        )

        canvas.restore()
    }

    fun addRevealedArea(point: GeoPoint, map: MapView) {
        revealedAreas.add(point)
        map.invalidate()
    }

    private fun metersToPixels(map: MapView, geo: GeoPoint, meters: Double): Double {
        val projection = map.projection

        val center = projection.toPixels(geo, null)
        val east = GeoPoint(geo.latitude, geo.longitude + 0.001)
        val eastPx = projection.toPixels(east, null)

        val pixels = kotlin.math.abs(eastPx.x - center.x)
        val metersAtLat = 111320 * cos(Math.toRadians(geo.latitude))

        return meters * (pixels / metersAtLat)
    }


}