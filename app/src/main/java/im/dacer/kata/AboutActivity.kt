package im.dacer.kata

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import me.drakeet.multitype.Items
import me.drakeet.support.about.*

class AboutActivity : me.drakeet.support.about.AboutActivity() {

    override fun onCreateHeader(icon: ImageView, slogan: TextView, version: TextView) {
        icon.setImageResource(R.drawable.icon)
        slogan.setText(R.string.app_name)
        version.text = BuildConfig.VERSION_NAME
    }

    override fun onItemsCreated(items: Items) {
        items.add(Category(s(R.string.about)))
        items.add(Card(s(R.string.description), s(R.string.review)))

        items.add(Line())

        items.add(Category(s(R.string.developer)))
        items.add(Contributor(R.mipmap.ic_launcher, "Dacer", "頑張ってね (๑•̀ㅂ•́)و✧"))

        items.add(Line())

        items.add(Category("Licenses"))
        items.add(License("JMdict", "JMdict", CC_LICENSE,
                "http://www.edrdg.org/edrdg/licence.html"))
        items.add(License("Kuromoji", "Kuromoji", License.APACHE_2,
                "https://github.com/atilika/kuromoji"))
        items.add(License("BigBang", "baoyongzhang", License.MIT,
                "https://github.com/baoyongzhang/BigBang"))
        items.add(License("Treasure", "baoyongzhang", License.MIT,
                "https://github.com/baoyongzhang/Treasure"))
        items.add(License("OkHttp", "square", License.APACHE_2,
                "https://github.com/square/okhttp"))
        items.add(License("MultiType", "drakeet", License.APACHE_2,
                "https://github.com/drakeet/MultiType"))
        items.add(License("about-page", "drakeet", License.APACHE_2,
                "https://github.com/drakeet/about-page"))
    }

    override fun onActionClick(action: View?) {
        super.onActionClick(action)
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/dacer/Kata"))
//        startActivity(intent)
        goGooglePlay()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun s(resId: Int): String = getString(resId)


    private fun goGooglePlay() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")))
        }
    }

    companion object {
        private val CC_LICENSE = "Creative Commons Attribution-ShareAlike Licence (V3.0)"
    }
}
