package br.com.ntxdev.zup;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import br.com.ntxdev.zup.domain.SolicitacaoListItem;
import br.com.ntxdev.zup.util.FontUtils;
import br.com.ntxdev.zup.util.ImageUtils;
import br.com.ntxdev.zup.widget.ImagePagerAdapter;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.PageIndicator;

public class SolicitacaoDetalheActivity extends FragmentActivity {

    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solicitacao_detalhe);

        SolicitacaoListItem solicitacao = (SolicitacaoListItem) getIntent().getExtras().getSerializable("solicitacao");
		boolean alterarLabel = getIntent().getExtras().getBoolean("alterar_botao", false);

		TextView protocolo = (TextView) findViewById(R.id.protocolo);
		protocolo.setText(getString(R.string.protocolo) + " " + solicitacao.getProtocolo());
		protocolo.setTypeface(FontUtils.getBold(this));

		TextView titulo = (TextView) findViewById(R.id.titulo);
		titulo.setText(solicitacao.getTitulo());
		titulo.setTypeface(FontUtils.getLight(this));

        TextView endereco = (TextView) findViewById(R.id.endereco);
        endereco.setText(solicitacao.getEndereco());
        endereco.setTypeface(FontUtils.getLight(this));

		TextView data = (TextView) findViewById(R.id.data);
		data.setText(getString(R.string.enviada) + " " + solicitacao.getData());
		data.setTypeface(FontUtils.getBold(this));

		ViewPager mPager = (ViewPager) findViewById(R.id.pager);
		if (solicitacao.getFotos().isEmpty()) {
			mPager.setVisibility(View.GONE);
            findViewById(R.id.indicator).setVisibility(View.GONE);

            GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            map.getUiSettings().setAllGesturesEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(false);

            CameraPosition p = new CameraPosition.Builder().target(new LatLng(solicitacao.getLatitude(),
                    solicitacao.getLongitude())).zoom(15).build();
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(p);
            map.moveCamera(update);

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(solicitacao.getLatitude(), solicitacao.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getScaled(this, solicitacao.getCategoria().getMarcador()))));
		} else {
            findViewById(R.id.map).setVisibility(View.GONE);
			ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), solicitacao.getFotos());
			mPager.setAdapter(mAdapter);
			PageIndicator mIndicator = (IconPageIndicator) findViewById(R.id.indicator);
			mIndicator.setViewPager(mPager);
		}		

		TextView comentario = (TextView) findViewById(R.id.comentario);
		comentario.setTypeface(FontUtils.getRegular(this));
		comentario.setText(solicitacao.getComentario());

		TextView botaoVoltar = (TextView) findViewById(R.id.botaoVoltar);
		if (alterarLabel) {
			botaoVoltar.setText(R.string.solicitaces_maiusculo);
		}
		botaoVoltar.setTypeface(FontUtils.getRegular(this));
		botaoVoltar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		TextView indicadorStatus = (TextView) findViewById(R.id.indicadorStatus);
		indicadorStatus.setTypeface(FontUtils.getBold(this));
		int fiveDp = (int) ImageUtils.dpToPx(this, 5);
		int tenDp = (int) ImageUtils.dpToPx(this, 10);
		indicadorStatus.setPadding(tenDp, fiveDp, tenDp, fiveDp);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			indicadorStatus.setBackgroundDrawable(ImageUtils.getStatusBackground(this, solicitacao.getStatus().getCor()));
		} else {
			indicadorStatus.setBackground(ImageUtils.getStatusBackground(this, solicitacao.getStatus().getCor()));
		}
		indicadorStatus.setText(solicitacao.getStatus().getNome());
	}
}