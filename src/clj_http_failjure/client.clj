(ns clj-http-failjure.client
  (:require [clj-http.client :as client]
            [clj-http.util :as util]
            [failjure.core :as f])
  (:refer-clojure :exclude [get]))

; From https://github.com/dakrone/clj-http/blob/aee13b4f544e83e4eb6014d6d1f9178676899f13/src/clj_http/client.clj#L1158-L1165
(defn- request*
  [req [respond raise]]
  (if (util/opt req :async)
    (if (some nil? [respond raise])
      (throw (IllegalArgumentException.
              "If :async? is true, you must pass respond and raise"))
      (client/request (dissoc req :respond :raise) respond raise))
    (client/request req)))

(defmacro def-request-method [method-symbol]
  `(defn ~method-symbol
     "Like #'request, but sets the :method and :url as appropriate."
     [~'url & [~'req & ~'r]]
     (client/check-url! ~'url)
     (let [~'req (merge ~'req {:method ~(keyword method-symbol)
                               :url ~'url})]
       (f/try*
         (request* ~'req ~'r)))))

(def-request-method get)
(def-request-method head)
(def-request-method post)
(def-request-method put)
(def-request-method delete)
(def-request-method options)
(def-request-method copy)
(def-request-method move)
(def-request-method patch)
