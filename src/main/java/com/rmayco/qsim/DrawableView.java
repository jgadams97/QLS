package com.rmayco.qsim;
import android.view.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.graphics.drawable.*;

public class DrawableView extends View {
	public static int LEFT, TOP;
	public static Drawable[] SPRITES = new Drawable[37];


	public DrawableView(Context ctx) {
		super(ctx);
		SPRITES[ 0] = ctx.getResources().getDrawable(R.drawable.qubit_row);
		SPRITES[ 1] = ctx.getResources().getDrawable(R.drawable.cell);
		SPRITES[ 2] = ctx.getResources().getDrawable(R.drawable.save);
		SPRITES[ 3] = ctx.getResources().getDrawable(R.drawable.load);
		SPRITES[ 4] = ctx.getResources().getDrawable(R.drawable.run);
		SPRITES[ 5] = ctx.getResources().getDrawable(R.drawable.black_separator);
		SPRITES[ 6] = ctx.getResources().getDrawable(R.drawable.x);
		SPRITES[ 7] = ctx.getResources().getDrawable(R.drawable.y);
		SPRITES[ 8] = ctx.getResources().getDrawable(R.drawable.z);
		SPRITES[ 9] = ctx.getResources().getDrawable(R.drawable.t);
		SPRITES[10] = ctx.getResources().getDrawable(R.drawable.tdg);
		SPRITES[11] = ctx.getResources().getDrawable(R.drawable.s);
		SPRITES[12] = ctx.getResources().getDrawable(R.drawable.sdg);

		SPRITES[13] = ctx.getResources().getDrawable(R.drawable.hadamard);
		SPRITES[14] = ctx.getResources().getDrawable(R.drawable.swap);
		SPRITES[15] = ctx.getResources().getDrawable(R.drawable.cnot);
		SPRITES[16] = ctx.getResources().getDrawable(R.drawable.fredkin);
		SPRITES[17] = ctx.getResources().getDrawable(R.drawable.toffoli);

		SPRITES[18] = ctx.getResources().getDrawable(R.drawable.x_block);
		SPRITES[19] = ctx.getResources().getDrawable(R.drawable.y_block);
		SPRITES[20] = ctx.getResources().getDrawable(R.drawable.z_block);
		SPRITES[21] = ctx.getResources().getDrawable(R.drawable.t_block);
		SPRITES[22] = ctx.getResources().getDrawable(R.drawable.tdg_block);
		SPRITES[23] = ctx.getResources().getDrawable(R.drawable.s_block);
		SPRITES[24] = ctx.getResources().getDrawable(R.drawable.sdg_block);
		SPRITES[25] = ctx.getResources().getDrawable(R.drawable.hadamard_block);
		SPRITES[26] = ctx.getResources().getDrawable(R.drawable.swap_block);
		SPRITES[27] = ctx.getResources().getDrawable(R.drawable.cnot_block_c);
		SPRITES[28] = ctx.getResources().getDrawable(R.drawable.cnot_block_x);
		SPRITES[29] = ctx.getResources().getDrawable(R.drawable.fredkin_block_c);
		SPRITES[30] = ctx.getResources().getDrawable(R.drawable.fredkin_block_sw);
		SPRITES[31] = ctx.getResources().getDrawable(R.drawable.toffoli_block_c);
		SPRITES[32] = ctx.getResources().getDrawable(R.drawable.toffoli_block_x);

		SPRITES[33] = ctx.getResources().getDrawable(R.drawable.zero_block);
		SPRITES[34] = ctx.getResources().getDrawable(R.drawable.one_block);

		SPRITES[35] = ctx.getResources().getDrawable(R.drawable.back);
		SPRITES[36] = ctx.getResources().getDrawable(R.drawable.save2);
		Core.main();
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		int[] location = new int[2];
		getLocationOnScreen(location);
		DrawableView.LEFT = location[0];
		DrawableView.TOP = location[1];
		Display.__init__(this, c, getWidth(), getHeight(), MainActivity.getScreenWidth(), MainActivity.getScreenHeight());
	}
}
