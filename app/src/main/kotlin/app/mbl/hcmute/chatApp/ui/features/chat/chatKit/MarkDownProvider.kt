package app.mbl.hcmute.chatApp.ui.features.chat.chatKit

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

class MarkDownProvider {
    companion object {
        var instance: Markwon? = null

        fun initMarkDown(context: Context) {
            if (instance == null) instance = Markwon.builder(context)
                // create default instance of TablePlugin
//                .usePlugin(TablePlugin.create(context))
//                .usePlugin(MarkwonInlineParserPlugin.create())
//                .usePlugin(JLatexMathPlugin.create(context.resources.displayMetrics.widthPixels.toFloat()) { builder ->
//                    builder.inlinesEnabled(true) // ENABLE inlines
//                })
                .build()
        }

        fun clear() {
            instance = null
        }
    }
}