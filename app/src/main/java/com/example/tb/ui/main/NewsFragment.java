package com.example.tb.ui.main;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.tb.GetSource;
import com.example.tb.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

//public class NewsFragment extends Fragment {
//
//    private static final String ARG_SECTION_NUMBER = "section_number";
//    private static List<String> source;
//
//    private PageViewModel pageViewModel;
//
//    public static NewsFragment newInstance(int index, List<String> l) {
//        NewsFragment fragment = new NewsFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_SECTION_NUMBER, index);
//        fragment.setArguments(bundle);
//        source = l;
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt("type");
//        }
//        pageViewModel.setIndex(index, source);
//    }
//
//    @Override
//    public View onCreateView(
//            @NonNull LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_main, container, false);
//        final TextView textView = root.findViewById(R.id.section_label);
//        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }
//}


public class NewsFragment extends Fragment {
    //private FloatingActionButton fab;
    private ListView listView;
    private PageViewModel pageViewModel;
    //private SwipeRefreshLayout swipeRefreshLayout;
    //private List<NewsBean.ResultBean.DataBean> list;
    private ArrayList<String> list;
    private static final int UPNEWS_INSERT = 0;
    @SuppressLint("HandlerLeak")
    private Handler newsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String uniquekey,title,date, category,author_name,url,thumbnail_pic_s,thumbnail_pic_s02,thumbnail_pic_s03;
            switch (msg.what){
                case UPNEWS_INSERT:
                    list = (ArrayList<String>) msg.obj ;
                    ListAdapter adapter = new ListAdapter(getActivity(),list);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
              index = getArguments().getInt("type");
        }
        pageViewModel.setIndex(index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        listView = (ListView) view.findViewById(R.id.listView);
        //fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        return view;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onAttach(getActivity());
        //获取传递的值
        Bundle bundle = getArguments();
//        //置顶功能
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listView.smoothScrollToPosition(0);
//            }
//        });
        //下拉刷新
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorRed);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                        // 下一步实现从数据库中读取数据刷新到listview适配器中
//                    }
//                },1000);
//            }
//        });
        //异步加载数据
        getDataFromNet();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取点击条目的路径，传值显示webview页面
//                String url = list.get(position).getUrl();
//                String uniquekey = list.get(position).getUniquekey();
//                final NewsBean.ResultBean.DataBean dataBean = (NewsBean.ResultBean.DataBean) list.get(position);
//                Intent intent = new Intent(getActivity(),WebActivity.class);
//                intent.putExtra("url",url);
//                startActivity(intent);

            }
        });
    }
    private void getDataFromNet(){
        final GetSource gs = new GetSource();
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return gs.getRaw();
            }
            protected void onPostExecute(final String result){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = newsHandler.obtainMessage();
                        msg.what = UPNEWS_INSERT;
                        msg.obj = gs.getEpidemic();
                        newsHandler.sendMessage(msg);
                    }
                }).start();
            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        };
        task.execute();
    }
}