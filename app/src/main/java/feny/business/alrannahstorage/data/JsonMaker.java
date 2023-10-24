package feny.business.alrannahstorage.data;

import org.apache.commons.lang3.StringUtils;

import java.util.Vector;

import kotlin.Pair;

public class JsonMaker {
    Vector<Pair<String,String>> jsonItems = new Vector<>();
    public void addItem(String key,String value){
        jsonItems.add(new Pair<>(key,value));
    }
    public String getJson(){
        String json = "{";
        for (Pair<String,String> item : jsonItems){
            json += "\""+item.getFirst()+"\":";
            json += "\""+item.getSecond()+"\",";
        }
        if(json.charAt(json.length()-1)==',') json = StringUtils.chop(json);
        json += "}";
        return json;
    }

    public String returnWithQuotation(String input){
        String output = "\""+input+"\"";
        return output;
    }
    public String getIntJson(){
        String json = "{";
        for (Pair<String,String> item : jsonItems){
            json += "\""+item.getFirst()+"\":";
            json += ""+item.getSecond()+",";
        }
        if(json.charAt(json.length()-1)==',') json = StringUtils.chop(json);
        json += "}";
        return json;
    }

}