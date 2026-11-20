package com.santosh.driver;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.InputType;
import java.util.Locale;

public class MainActivity extends Activity {
    int trip = 0;
    int stage = 0; // 0 request, 1 accepted, 2 PIN verified, 3 in progress, 4 completed
    int completed = 0;
    int earnings = 0;
    TextView status, fare, pickup, drop, distance, actionTitle, earningsView, tripsView;
    LinearLayout content;
    EditText pinInput;
    final String[] pickups = {"Satellite, Ahmedabad", "Prahlad Nagar, Ahmedabad", "Navrangpura, Ahmedabad", "Vastrapur, Ahmedabad"};
    final String[] drops = {"Prahlad Nagar, Ahmedabad", "Bodakdev, Ahmedabad", "CG Road, Ahmedabad", "Thaltej, Ahmedabad"};
    final int[] fares = {185, 142, 210, 165};
    final String demoPin = "4826";

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        showScreen();
    }
    int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density); }
    TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setGravity(Gravity.CENTER_VERTICAL); return t;
    }
    GradientLayout card() { GradientLayout v = new GradientLayout(); v.setOrientation(LinearLayout.VERTICAL); v.setPadding(dp(16),dp(14),dp(16),dp(14)); v.setBackgroundColor(Color.WHITE); return v; }
    class GradientLayout extends LinearLayout { GradientLayout(){super(MainActivity.this);} }
    void addSpace(LinearLayout l, int h) { View v = new View(this); l.addView(v,new LinearLayout.LayoutParams(1,dp(h))); }
    void addLine(LinearLayout l) { View v = new View(this); v.setBackgroundColor(Color.rgb(229,231,235)); l.addView(v,new LinearLayout.LayoutParams(-1,dp(1))); addSpace(l,10); }
    Button button(String label, int color) {
        Button b = new Button(this); b.setText(label); b.setTextColor(Color.WHITE); b.setAllCaps(false);
        b.setTextSize(16); b.setTypeface(Typeface.DEFAULT,Typeface.BOLD); b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
        return b;
    }
    void showScreen() {
        LinearLayout outer = new LinearLayout(this); outer.setOrientation(LinearLayout.VERTICAL); outer.setBackgroundColor(Color.rgb(244,246,248));
        LinearLayout header = new LinearLayout(this); header.setOrientation(LinearLayout.VERTICAL); header.setPadding(dp(18),dp(18),dp(18),dp(14)); header.setBackgroundColor(Color.rgb(16,24,39));
        TextView brand = text("SANTOSH DRIVER",22,Color.WHITE,true); header.addView(brand);
        TextView sub = text("DRIVER PRACTICE  •  DEMO ONLY",11,Color.rgb(167,243,208),true); header.addView(sub);
        outer.addView(header);
        ScrollView scroll = new ScrollView(this); content = new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(dp(16),dp(16),dp(16),dp(24)); scroll.addView(content); outer.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        setContentView(outer);

        LinearLayout stats = new LinearLayout(this); stats.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout e = card(), t = card(); e.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1)); t.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1)); 
        e.addView(text("DEMO EARNINGS",11,Color.GRAY,true)); earningsView=text("₹"+earnings,23,Color.rgb(17,24,39),true); e.addView(earningsView);
        t.addView(text("TRIPS COMPLETED",11,Color.GRAY,true)); tripsView=text(""+completed,23,Color.rgb(17,24,39),true); t.addView(tripsView);
        stats.addView(e); View gap=new View(this); stats.addView(gap,new LinearLayout.LayoutParams(dp(10),1)); stats.addView(t); content.addView(stats);
        addSpace(content,14);

        LinearLayout map = card(); map.setBackgroundColor(Color.rgb(225,235,229));
        TextView mapTitle=text("⌖  TRIP AREA",12,Color.rgb(55,65,81),true); map.addView(mapTitle);
        TextView mapArt=text("      ───────╱─────\n   ╱       ●         ╲\n  │   AHMEDABAD      │\n   ╲      ────       ╱\n      ─────────",19,Color.rgb(55,85,70),true); mapArt.setGravity(Gravity.CENTER); map.addView(mapArt);
        TextView mapNote=text("Illustrative map • No live navigation",10,Color.DKGRAY,false); mapNote.setGravity(Gravity.CENTER); map.addView(mapNote); content.addView(map);
        addSpace(content,14);

        LinearLayout panel=card(); content.addView(panel);
        String[] stageNames={"New trip request","Trip accepted","Verify passenger PIN","Trip in progress","Trip completed"};
        status=text(stageNames[stage],18,Color.rgb(17,24,39),true); panel.addView(status);
        TextView demo=text("PRACTICE TRIP  #"+(trip+1),11,Color.rgb(22,163,74),true); panel.addView(demo); addSpace(panel,10);
        fare=text("₹"+fares[trip%fares.length]+"  •  estimated demo fare",18,Color.rgb(17,24,39),true); panel.addView(fare); addSpace(panel,12);
        addLine(panel);
        panel.addView(text("●  PICKUP",11,Color.rgb(22,163,74),true)); pickup=text(pickups[trip%pickups.length],15,Color.DKGRAY,true); panel.addView(pickup); addSpace(panel,12);
        panel.addView(text("●  DROP-OFF",11,Color.rgb(217,119,6),true)); drop=text(drops[trip%drops.length],15,Color.DKGRAY,true); panel.addView(drop); addSpace(panel,12);
        distance=text("4.5 km  •  approx. 18 min",13,Color.GRAY,false); panel.addView(distance); addSpace(panel,16);
        if(stage==0) {
            Button accept=button("ACCEPT TRIP  •  DEMO",Color.rgb(22,163,74)); panel.addView(accept,new LinearLayout.LayoutParams(-1,-2));
            accept.setOnClickListener(v->{stage=1;showScreen();});
            Button reject=button("Skip this request",Color.rgb(75,85,99)); panel.addView(reject,new LinearLayout.LayoutParams(-1,-2));
            reject.setOnClickListener(v->{nextTrip();});
        } else if(stage==1) {
            panel.addView(text("Go to the pickup point. In a real service, follow the app's navigation.",13,Color.DKGRAY,false)); addSpace(panel,10);
            Button arrived=button("I HAVE ARRIVED",Color.rgb(22,163,74)); panel.addView(arrived); arrived.setOnClickListener(v->{stage=2;showScreen();});
        } else if(stage==2) {
            panel.addView(text("Ask the passenger for the 4-digit demo PIN.",13,Color.DKGRAY,false)); addSpace(panel,8);
            pinInput=new EditText(this); pinInput.setSingleLine(true); pinInput.setHint("Enter demo PIN"); pinInput.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD); panel.addView(pinInput);
            TextView hint=text("Training PIN: 4826",12,Color.GRAY,false); panel.addView(hint); addSpace(panel,8);
            Button verify=button("VERIFY PIN",Color.rgb(22,163,74)); panel.addView(verify); verify.setOnClickListener(v->{if(demoPin.equals(pinInput.getText().toString())){stage=3;showScreen();}else Toast.makeText(this,"Incorrect demo PIN. Try 4826.",Toast.LENGTH_SHORT).show();});
        } else if(stage==3) {
            panel.addView(text("Practice driving to the drop-off location.",13,Color.DKGRAY,false)); addSpace(panel,10);
            Button finish=button("COMPLETE TRIP",Color.rgb(22,163,74)); panel.addView(finish); finish.setOnClickListener(v->{stage=4;completed++;earnings+=fares[trip%fares.length];showScreen();});
        } else {
            TextView done=text("✓  Great work! This demo trip is complete.",15,Color.rgb(22,101,52),true); panel.addView(done); addSpace(panel,10);
            Button next=button("NEXT PRACTICE TRIP",Color.rgb(22,163,74)); panel.addView(next); next.setOnClickListener(v->nextTrip());
        }
        addSpace(content,16);
        TextView disclaimer=text("This is an independent educational simulator. Not affiliated with, endorsed by, or connected to Uber. No real rides, accounts, payments, PINs or navigation.",11,Color.GRAY,false);
        content.addView(disclaimer);
    }
    void nextTrip(){trip++;stage=0;showScreen();}
}
