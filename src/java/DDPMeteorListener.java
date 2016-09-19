/*
* (c)Copyright 2013-2014 Ken Yee, KEY Enterprise Solutions 
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.keysolutions.ddpclient.meteor;

import com.keysolutions.ddpclient.DDPListener;

import java.util.Map;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Listener for method errors/results/updates
 * @author kenyee
 */
public class DDPMeteorListener extends DDPListener implements Observer {

public String lastMessage;
public String timestampOfLastMessage;
public String lastInternalLog;
public String lastUpdate;

    public DDPMeteorListener(){
	lastMessage = "";
    }


    public void update(Observable client, Object msg){
        lastUpdate = msg.toString();
        lastInternalLog = "client: "+client.toString()+" msg:"+msg.toString();
    }

    public String getLastMessage(){
	return lastMessage;
    }

    public String getLastUpdate(){
        return lastUpdate;
    }

    public String getLastInternalLog(){
	return lastInternalLog;
    }

    public String getTimestampOfLastMessage(){
	return timestampOfLastMessage;
    }

    /**
     * Callback for method call with all result fields
     * @param resultFields returned results from method call
     */
    public void onResult(Map<String, Object> resultFields) {
	lastMessage=resultFields.toString();
	timestampOfLastMessage = new SimpleDateFormat("MM/dd HH:mm:ss").format(new Date());
	lastInternalLog="onResult";
}
    
    /**
     * Callback for method's "updated" event
     * @param callId method call ID
     */
    public void onUpdated(String callId) {
	lastInternalLog = lastInternalLog+ "Updated :"+callId;
}
    
    /**
     * Callback for method's "ready" event (for subscriptions)
     * @param callId method call ID
     */
    public void onReady(String callId) {
        String timestamp = new SimpleDateFormat("MM/dd HH:mm:ss").format(new Date());

	lastInternalLog ="Ready : "+callId+" : "+timestamp+" | ";
}
    
    /**
     * Callback for invalid subscription name errors
     * @param callId method call ID
     * @param errorFields fields holding error info
     */
    public void onNoSub(String callId, Map<String, Object> errorFields) {
	lastMessage = errorFields.toString();
	lastInternalLog = "NoSub : "+callId;
}
    
    /**
     * Callback for receiving a Pong back from the server
     * @param pingId ping ID (mandatory)
     */
    public void onPong(String pingId) {
	lastInternalLog = "Pong : "+pingId;
}
}
