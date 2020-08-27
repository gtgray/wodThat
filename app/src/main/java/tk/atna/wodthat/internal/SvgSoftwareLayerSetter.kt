package tripally.app.internal

import android.graphics.drawable.Drawable
import android.widget.ImageView

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target

/**
 * Listener which updates the {@link ImageView} to be software rendered, because {@link
 * com.caverock.androidsvg.SVG SVG}/{@link android.graphics.Picture Picture} can't render on a
 * hardware backed {@link android.graphics.Canvas Canvas}.
 */
class SvgSoftwareLayerSetter<T : Drawable> : RequestListener<T> {

    override fun onLoadFailed(e : GlideException?, model : Any?, target : Target<T>?, isFirstResource : Boolean) : Boolean {
        val view = (target as ImageViewTarget).view
        view.setLayerType(ImageView.LAYER_TYPE_NONE, null)
        return false
    }

    override fun onResourceReady(resource : T, model : Any?, target : Target<T>?, dataSource : DataSource?, isFirstResource : Boolean) : Boolean {
        val view = (target as ImageViewTarget).view
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
        return false
    }

}