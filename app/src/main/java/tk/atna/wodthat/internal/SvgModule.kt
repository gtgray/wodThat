package tripally.app.internal

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import java.io.InputStream

@GlideModule
class SvgModule : AppGlideModule() {

    override fun registerComponents(context : Context, glide : Glide, registry : Registry) {
        // regular svg into image view placing
        registry.register(SVG::class.java, PictureDrawable::class.java, SvgPictureTranscoder())
        // svg into tintable image view placing
        registry.register(SVG::class.java, BitmapDrawable::class.java, SvgBitmapTranscoder(context))
        registry.append(Registry.BUCKET_BITMAP, InputStream::class.java, SVG::class.java, SvgDecoder())
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled() : Boolean = false

}