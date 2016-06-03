(ns meteor-load-test.method_calls
  (:use [meteor-load-test util ddp_action]))
;  (:require [clj-time.core :as t] ) 
(def invalid-calls-msg "Property must be of form: 
     grinder.calls = [<method-call>, <method-call>, etc...]
                 where <method-call> is one of: 
                   * \"method-name\" - ex. [\"keepAlive\"] 
                   * {\"addEntry\":[{\"ownerId\":\"CLIENTID\",\"name\":\"load-RUNID\",\"type\":\"client\"}]} ")

(defn check-last-message
    ([ddpmeteorlistener]
	
      (let [lastMessage (. ddpmeteorlistener (getLastMessage))]
    	(log "Checking Last Message: " lastMessage) 
    	(if (.contains lastMessage "Error")
		(log (apply str ["--FAILURE--" lastMessage]) )
		(log (apply str ["--SUCCESS--" lastMessage]) )
	)
      )
      (let [timestampOfLastMessage (. ddpmeteorlistener (getTimestampOfLastMessage))]
		(log "Timestamp of Last Message: " timestampOfLastMessage)
      )
    )
)

(defn call-method 
  "Calls a Meteor method. Converts args to an Object[] 
   before passing to DDP client"
  ([ddp ddpmeteorlistener method-name]
    (log "calling: " method-name)
 ;   (let [start-time (t/now)]
    	(.call ddp method-name (object-array []) ddpmeteorlistener)
    	(check-last-message ddpmeteorlistener)
;	(log "Call Response Time:" (t/in-millis (t/interval start-time (t/now))))
  )
  ([ddp ddpmeteorlistener method-name v]
    (log "calling: " method-name " with args: " v)
    (.call ddp method-name (object-array v) ddpmeteorlistener)
    (check-last-message ddpmeteorlistener)
  )
)

(defn perform-calls 
  "Calls meteor methods using supplied fn. Valid elements 
   of seq s include: 
     * string = method name with no parameters
     * map = of form 'method-name':[arg1, arg2, etc.]"
  [sleep call-method-fn s]
  (perform-ddp-action sleep invalid-calls-msg call-method-fn s))

