package tk.atna.wodthat.stuff

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import tk.atna.wodthat.R
import tripally.app.internal.SvgSoftwareLayerSetter


fun placeSvgBitmapImage(view : ImageView?, url : String) {
    if(view != null) {
        val context = view.context
        Glide.with(context)
                .`as`(BitmapDrawable::class.java)
                .load(Uri.parse(url))
                .transition(DrawableTransitionOptions.withCrossFade(
                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                    .placeholder(ColorDrawable(Utils.getColor(context, R.color.clr_back)))
//                    .error(R.drawable.image_error)
                .listener(SvgSoftwareLayerSetter<BitmapDrawable>())
                .into(view)
    }
}

fun placeSvgImage(view : ImageView?, url : String) {
    view?.let {
        val context = view.context
        Glide.with(context)
                .`as`(PictureDrawable::class.java)
                .load(Uri.parse(url))
                .transition(DrawableTransitionOptions.withCrossFade(
                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                    .placeholder(ColorDrawable(Utils.getColor(context, R.color.clr_back)))
//                    .error(R.drawable.image_error)
                .listener(SvgSoftwareLayerSetter<PictureDrawable>())
                .into(view)
    }
}

fun placeImage(view : ImageView?, url : String?) {
    view?.let {
        val context = view.context
        Glide.with(context)
            .load(if (url != null) Uri.parse(url) else url)
            .transition(
                DrawableTransitionOptions.withCrossFade(
                    DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                )
            )
            .diskCacheStrategy(DiskCacheStrategy.DATA)
//            .placeholder(R.drawable.svg_kettlebell)
//            .fallback(R.drawable.svg_kettlebell)
            .placeholder(ColorDrawable(getColor(context, R.color.clr_transparent)))
            .fallback(ColorDrawable(getColor(context, R.color.clr_accent_55)))
//                .placeholder(ColorDrawable(Utils.getColor(context, R.color.clr_back)))
//                .error(R.drawable.image_error)
            .into(view)
    }
}

//    fun placeImage(view : ImageView?, url : String) {
//        view?.let {
//            val context = view.context
//            Glide.with(context)
////                .asDrawable()
//                .load(Uri.parse(url))
//                .transition(
//                    DrawableTransitionOptions.withCrossFade(
//                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
//                    )
//                )
//                .listener(object: RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Drawable>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        Log.e("----------------- ERROR $e")
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable?,
//                        model: Any?,
//                        target: Target<Drawable>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        Log.e("----------------- READY $resource")
//                        return false
//                    }
//                })
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .placeholder(R.drawable.svg_kettlebell)
//                .into(view)
//        }
//    }

fun clearTarget(view: ImageView?) {
    view?.let {
        Glide.with(view.context)
            .clear(view)
    }
}


interface OnImageLoadedListener<T> {

    fun onImageReady(image : T)

    fun onFailed(e : Exception) {
        // skip processing
    }

}

