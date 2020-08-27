package tk.atna.wodthat.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import tk.atna.wodthat.R
import tk.atna.wodthat.internal.textSize
import tk.atna.wodthat.stuff.dpToPixels
import tk.atna.wodthat.stuff.getColor

class TextDialog(context : Context) : AlertDialog(context/*, R.style.Style_Dialog*/) {

    private var tvTitle : TextView? = null
    private var tvMessage : TextView? = null

    companion object {
        fun show(
            context: Context,
            title: String? = null,
            text: String,
            cancelable: Boolean = false,
            withBtn: Boolean = true
        ) {
            create(context, title, text, cancelable, withBtn).show()
        }

        fun create(
            context: Context,
            title: String? = null,
            text: String,
            cancelable: Boolean = false,
            withBtn: Boolean = true
        ): TextDialog {
            val dialog = TextDialog(context)
            with(dialog) {
                title?.let { setTitle(title) }
                setMessage(Html.fromHtml(text))
                setCancelable(cancelable)
                // add button
                if(withBtn) {
                    setButton(
                        DialogInterface.BUTTON_POSITIVE,
                        context.getString(R.string.ok), null as DialogInterface.OnClickListener?
                    )
                }
            }
            //
            return dialog
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        // tweak a little system message text view
        tvMessage = findViewById(android.R.id.message)
        tvMessage?.let {
            tvMessage!!.textSize(R.dimen.text_14)
            tvMessage!!.setTextColor(getColor(context, R.color.clr_black_55))
            tvMessage!!.isVerticalScrollBarEnabled = true
        }
        // tweak title
        tvTitle = findViewById(R.id.alertTitle)
        tvTitle?.let {
            val vg = tvTitle!!.parent as? ViewGroup
            vg?.let {
                val lp = vg.layoutParams as LinearLayout.LayoutParams
                lp.bottomMargin = dpToPixels(context, 16)
            }
        }
    }

}
