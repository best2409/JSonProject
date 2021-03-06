package com.example.jsonproject;

import java.io.*;
import java.net.*;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.json.*;

import android.app.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity {

	private EditText etMessage;
	private Button btnSend;
	private TextView tvRecvData1, tvRecvData2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 /*if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);

			안드로이드 버전 3.0 이상부터는 인터넷 연결은 쓰레드나 핸들러에서 처리하도록 정책이 바뀌었다. 그래서 UI 쓰레스에서 인터넷 연결을
			시도하면(HttpURLConnection과 같은 것으로) 실행 타임에서 에러가 발생한다.

		    @Override
		    public void onCreate(Bundle savedInstanceState) {
		     if (Build.VERSION.SDK_INT > 9){
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		     }
		     
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.get_signature_from_toodledo);
		        
		        txt = (TextView)findViewById(R.id.txt);
		    }
		  */
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		
		
		etMessage = (EditText) findViewById(R.id.et_message);
		btnSend = (Button) findViewById(R.id.btn_sendData);
		tvRecvData1 = (TextView)	findViewById(R.id.tv_recvData1);
		tvRecvData2 = (TextView)	findViewById(R.id.tv_recvData2);
		
		
		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				// 에뮬레이터는 자체 IP 사용 -> localhost 사용하면 안됨!
				String urlString = "http://10.0.0.2:8080/JSONProject/json.jsp";
				
				DefaultHttpClient client = new DefaultHttpClient();
					
				try {
					HttpPost post = new HttpPost(urlString);
					
					HttpResponse response = client.execute(post);
					
					int resCode = response.getStatusLine().getStatusCode();
					
					if(resCode == HttpURLConnection.HTTP_OK) {
						BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
						String line = null;
						String result = "";
			
						while ((line = bufreader.readLine()) != null) {
							result += line;
						}
					
						
						String[][] parseData = jsonParserList(result);
						
						tvRecvData1.setText(parseData[0][0]);
						tvRecvData2.setText(parseData[0][1]);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
		});
	}
	
	//  JSON Parsing
	private String[][] jsonParserList(String pRecvServerPage) {
		
		Log.e("msg : ", pRecvServerPage);
		
		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("List");


			// πﬁæ∆ø¬ pRecvServerPage∏¶ ∫–ºÆ«œ¥¬ ∫Œ∫–
			String[] jsonName = {"msg1", "msg2", "msg3"};
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			for (int i = 0; i < jArr.length(); i++) {
				json = jArr.getJSONObject(i);
				
				for(int j = 0; j < jsonName.length; j++) {
					parseredData[i][j] = json.getString(jsonName[j]);
				}
			}
			
			
			for(int i=0; i<parseredData.length; i++){
				Log.e("JSON-> "+i+" : ", parseredData[i][0]);
				Log.e("JSON-> "+i+" : ", parseredData[i][1]);
				Log.e("JSON-> "+i+" : ", parseredData[i][2]);
			}
			return parseredData;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}

		
		
	
		
		
