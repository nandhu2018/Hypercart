package hypercard.gigaappz.com.hypercart;

import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class Aboutus extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        Element versionElement = new Element();
        versionElement.setTitle("version v"+versionName);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(String.format(getString(R.string.copyright1)))
                .setImage(R.drawable.logo)
                .addItem(versionElement)
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("nandhuaravind@mca.ajce.in")
                .addWebsite("http://nandhugigaappz.com")
                .addFacebook("nandhu1996")
                .addYoutube("UCJgPQClX71pMq0rzzl0Rfuw")
                .addPlayStore("com.gigaappz.hypercart")
                .addGitHub("nandhu2018")
                .addInstagram("nandhu1996")
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copyright), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.mipmap.ic_launcher);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Aboutus.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

}
