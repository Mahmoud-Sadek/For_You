package com.a3shank.apps.ashank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3shank.apps.ashank.Activites.AboutUsActivity;
import com.a3shank.apps.ashank.Activites.HistoryActivity;
import com.a3shank.apps.ashank.Activites.LoginActivity;
import com.a3shank.apps.ashank.Activites.MainActivity;
import com.a3shank.apps.ashank.Activites.NotificationsActivity;
import com.a3shank.apps.ashank.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

import java.util.List;

import static com.a3shank.apps.ashank.Activites.MainActivity.freeDialog;
import static com.a3shank.apps.ashank.utils.Drawer.*;

/**
 * Created by Sadokey on 12/22/2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {

    int[] photo = {R.drawable.ic_home_whit, R.drawable.ic_notification, R.drawable.empty, R.drawable.empty, R.drawable.ashank_word, R.drawable.ic_language, R.drawable.logout};
    int[] text = {R.string.home, R.string.notifications, R.string.free_code, R.string.free_code, R.string.about, R.string.language, R.string.logout};
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title;
        ImageView img_poster;

        public MyViewHolder(View view) {
            super(view);
            txt_title = (TextView) view.findViewById(R.id.mnu_txt);
            img_poster = (ImageView) view.findViewById(R.id.mnu_photo);
        }
    }
    public DrawerAdapter(Context mContext) {
        this.mContext = mContext;
//        if (MainActivity.largeScreen) {
//        }
    }

    View itemView;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.img_poster.setImageResource(photo[position]);
        holder.txt_title.setText(text[position]);
        if (holder.getAdapterPosition()==2){
            itemView.setBackgroundResource(R.drawable.free_code);
        }
        if (holder.getAdapterPosition()==3){
            itemView.setBackgroundResource(R.drawable.yticket);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawers(holder.getAdapterPosition());
            }
        });
    }

    private void drawers(int position) {
        if (position == 5) {
            showChangeLangDialog();
        }
        if (position == 4) {
//                    DialogFragment dialog = AboutUsDialogFragment.newInstance();
//                    dialog.show(MainActivity.this.getFragmentManager(), "AboutUsDialogFragment");
            Intent intent = new Intent(mContext, AboutUsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        if (position == 1) {
            Intent intent = new Intent(mContext, NotificationsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        if (position == 0) {
            if (!activityName.equals("MainActivity")) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        }
        if (position == 3) {
            Intent intent = new Intent(mContext, HistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        if (position == 2) {

            if (!activityName.equals("MainActivity")) {
                freeDialog = true;
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
            else {
                freeDialog();
            }
//                    FlowingDrawer mDrawer = (FlowingDrawer) v.findViewById(R.id.drawerlayout);
//                    mDrawer.closeMenu();
//                    DialogFragment dialog = FreeCodeDialogFragment.newInstance(mContext);
//                    dialog.show(activity.getFragmentManager(), "FreeCodeDialogFragment");
        }
        if (position == 6) {
            auth.signOut();
            for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (user.getProviderId().equals("facebook.com")) {
                    LoginManager.getInstance().logOut();
                }
            }
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
            activity.finish();
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return photo.length;
    }


}

//
// extends BaseAdapter {
//    int[] photo = {R.drawable.ic_home_whit, R.drawable.ic_notification, R.drawable.empty, R.drawable.empty, R.drawable.ashank_word, R.drawable.ic_language, R.drawable.logout};
//    int[] text = {R.string.home, R.string.notifications, R.string.free_code, R.string.free_code, R.string.about, R.string.language, R.string.logout};
//    Context mContext;
////    Typeface myTypeface;
//    public DrawerAdapter(Context context) {
//        mContext = context;
////        myTypeface = Typeface.createFromAsset(mContext.getAssets(), "arabic2.otf");
//    }
//
//    @Override
//    public int getCount() {
//        return photo.length;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return photo[position];
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_drawer, parent, false);
//        }
//        ImageView mnuPhoto = (ImageView) convertView.findViewById(R.id.mnu_photo);
//        TextView mnuText = (TextView) convertView.findViewById(R.id.mnu_txt);
////        mnuText.setTypeface(myTypeface);
//        mnuPhoto.setImageResource(photo[position]);
//        mnuText.setText(text[position]);
//        if (position==2){
//            convertView.setBackgroundResource(R.drawable.free_code);
//        }
//        if (position==3){
//            convertView.setBackgroundResource(R.drawable.yticket);
//        }
//        return convertView;
//    }
//
//}