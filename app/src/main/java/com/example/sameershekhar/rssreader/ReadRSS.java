package com.example.sameershekhar.rssreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Created by sameershekhar on 08-Aug-17.
 */

public class ReadRSS extends AsyncTask<Void,Void,Void> {

    Context context;
    URL url;
    ArrayList<FeedItem> feeditemArray=new ArrayList<>();
    String address="http://newsrack.in/crawled.feeds/toi.rss.xml";
    ProgressDialog progressDialog;
    RecyclerView recyclerView;


    public ReadRSS(Context context,RecyclerView recylerView) {
        this.recyclerView=recylerView;
        this.context = context;
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading Data....");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        processXml(getData());
        return null;
    }

    private void processXml(Document data) {

        if(data!=null)
        {

            Log.d("root",data.getDocumentElement().getNodeName());
            Element root=data.getDocumentElement();
            Node channel=root.getChildNodes().item(1);
            NodeList items=channel.getChildNodes();
            for(int i=0;i<items.getLength();i++)
            {
                Node currentChild=items.item(i);
                if(currentChild.getNodeName().equalsIgnoreCase("item"))
                {
                    NodeList itemchilds=currentChild.getChildNodes();
                    FeedItem feeditem=new FeedItem();
                    for(int j=0;j<itemchilds.getLength();j++)
                    {
                        Node current=itemchilds.item(j);
                        if(current.getNodeName().equalsIgnoreCase("title"))
                        {
                            feeditem.setTitle(current.getTextContent());
                        }
                        else  if(current.getNodeName().equalsIgnoreCase("description"))
                        {
                            feeditem.setDescription(current.getTextContent());
                        }

                        else if(current.getNodeName().equalsIgnoreCase("link"))
                        {
                            feeditem.setLink(current.getTextContent());
                        }


                    }

                    feeditemArray.add(feeditem);

                }
            }


        }

    }


    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();

        MyAdaptor adaptor  = new MyAdaptor(context,feeditemArray);
       recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adaptor);
        //adapter.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent=new Intent(context,Details.class);
                        intent.putExtra("Uri", feeditemArray.get(position).getLink().toString());
                        context.startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    public Document getData()
    {

        try {
            url=new URL(address);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream=connection.getInputStream();
            DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=builderFactory.newDocumentBuilder();
            Document xmlDoc=builder.parse(inputStream);
            return xmlDoc;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
