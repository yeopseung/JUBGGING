package org.techtown.my_jubgging;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ViewHolder extends RecyclerView.ViewHolder {
    LinearLayout body;

    TextView recruitingTxt;

    TextView titleTxt;
    TextView contentTxt;

    TextView startTimeTxt;
    TextView peopleNumTxt;
    TextView uploadTimeTxt;

    TextView region1Txt;
    TextView region2Txt;
    TextView region3Txt;

    ViewHolder(View itemView) {
        super(itemView);

        body = itemView.findViewById(R.id.together_board_layout_body);

        recruitingTxt = itemView.findViewById(R.id.together_board_recruiting_text);

        titleTxt = itemView.findViewById(R.id.together_board_title_text);
        contentTxt = itemView.findViewById(R.id.together_board_content_text);

        startTimeTxt = itemView.findViewById(R.id.together_board_start_time);
        peopleNumTxt = itemView.findViewById(R.id.together_board_participants);
        uploadTimeTxt = itemView.findViewById(R.id.together_board_upload_time);

        region1Txt = itemView.findViewById(R.id.together_board_region1);
        region2Txt = itemView.findViewById(R.id.together_board_region2);
        region3Txt = itemView.findViewById(R.id.together_board_region3);
    }
}
