package tripally.app.internal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.caverock.androidsvg.SVG

/**
 * Convert the {@link SVG}'s internal representation to an Android-compatible one ({@link Picture}).
 */
class SvgBitmapTranscoder(val context : Context) : ResourceTranscoder<SVG, BitmapDrawable> {

    override fun transcode(toTranscode : Resource<SVG>, options : Options) : Resource<BitmapDrawable> {
        val svg = toTranscode.get()
        val width = svg.documentWidth.toInt()
        val height = svg.documentHeight.toInt()
        val picture = svg.renderToPicture(width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawPicture(picture)
        val drawable = BitmapDrawable(context.resources, bitmap)
        return SimpleResource<BitmapDrawable>(drawable)
    }

}