package com.gebulot.hcp_predictive_tutorial1.SAPAdmin;

import android.content.Context;
import android.util.Log;

import com.sap.maf.tools.logon.logonui.api.LogonUIFacade;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.IManagerConfigurator;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataContractViolationException;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataNetworkException;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataResponseSingle;

import java.net.URL;
import java.util.List;

/**
 * Created by Yony on 15/05/2016.
 */
public class SAPAdmin implements OnlineODataStore.OpenListener{

    public static SAPAdmin instance=new SAPAdmin();
    private static Context appContext;
    OnlineODataStore onlineODataStore;
    HttpConversationManager manager ;

    public SAPAdmin(){

    }


    public void init(Context ac,String sBaseURL){
        appContext=ac;

        try{
            manager = new HttpConversationManager(appContext);
            IManagerConfigurator configurator = LogonUIFacade.getInstance().getLogonConfigurator(appContext);
            configurator.configure(manager);
            OnlineODataStore.OnlineStoreOptions options=new OnlineODataStore.OnlineStoreOptions();
            URL baseURL=new URL(sBaseURL);
            //OnlineODataStore.ODataStoreOpenExecution openExecution = OnlineODataStore.open(appContext, baseURL, manager, this, options);
            OnlineODataStore.open(appContext, baseURL, manager, this, options);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public List<ODataEntity> executeRequest(String request) throws ODataParserException,ODataNetworkException,ODataContractViolationException{
        ODataProperty property;
        ODataPropMap properties;
        ODataResponseSingle resp = onlineODataStore.executeReadEntitySet(request, null);
        ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
        List<ODataEntity> entities = feed.getEntities();
        /*
        for (ODataEntity entity : entities)
        {
            properties = entity.getProperties();

            property = properties.get("PRODUCT_ID");
            String productID=property.getValue().toString();

            property = properties.get("PRODUCT_NAME");
            String productName=property.getValue().toString();

            //ETC
        }
        */
        return entities;
    }


    @Override
    public void storeOpened(OnlineODataStore onlineODataStore) {
        this.onlineODataStore=onlineODataStore;
        Log.v("SAPAdmin","storeOpened");
    }

    @Override
    public void storeOpenError(ODataException e) {
        Log.v("SAPAdmin","storeOpenError "+e);
    }
}
