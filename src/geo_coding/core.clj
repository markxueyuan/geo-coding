(ns geo-coding.core
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all]))

(def ^:dynamic *ak* "zPn3027FC4oE1TtC7Pqgp08e")

(defn get-coordinate
  [original-address & [city]]
  (let [body (:body (client/get "http://api.map.baidu.com/geocoder/v2/"
                         {:query-params {"address" original-address
                                         "output" "json"
                                         "ak" *ak*
                                         "city" city}}))
        body (parse-string body true)]
    (when (= 0 (:status body))
      (assoc (:result body) :original_address original-address))))

(get-coordinate "北京市丰台区四方景园一区1号楼2402")

(defn get-location
  [input]
  (when input
    (let [lng (get-in input [:location :lng])
          lat (get-in input [:location :lat])
          body (:body
                (client/get "http://api.map.baidu.com/geocoder/v2/"
                            {:query-params {"ak" *ak*
                                            "location" (str lat "," lng)
                                            "output" "json"
                                            "pois" "0"}}))
          body (parse-string body true)]
      (into
        (dissoc (:result body)
                :location
                )
        input))))

