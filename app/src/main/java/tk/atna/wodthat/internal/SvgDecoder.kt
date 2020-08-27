package tripally.app.internal

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.request.target.Target
import com.caverock.androidsvg.PreserveAspectRatio
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.IOException
import java.io.InputStream

/**
 * Decodes an SVG internal representation from an {@link InputStream}.
 */
class SvgDecoder : ResourceDecoder<InputStream, SVG> {

    override fun handles(source : InputStream, options : Options) : Boolean {
        // TODO: can we tell?
        return true
    }

    @Throws(IOException::class)
    override fun decode(source : InputStream, width : Int, height : Int, options : Options) : Resource<SVG> {
        try {
            val svg = SVG.getFromInputStream(source)
// before
//            svg.documentWidth = width.toFloat()
//            svg.documentHeight = height.toFloat()
//            svg.documentPreserveAspectRatio = PreserveAspectRatio.FULLSCREEN
// after https://github.com/bumptech/glide/commit/5a814a1ca472c6a174cefec7c2cb60a66a53321c
            if (width != Target.SIZE_ORIGINAL)
                svg.documentWidth = width.toFloat()
            if (height != Target.SIZE_ORIGINAL)
                svg.documentHeight = height.toFloat()
            return SimpleResource<SVG>(svg)
        //
        } catch (ex : SVGParseException) {
            throw IOException("Cannot load SVG from stream", ex)
        }
    }
}
