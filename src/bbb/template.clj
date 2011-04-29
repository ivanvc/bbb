(ns bbb.template
  (:require [net.cgrand.enlive-html :as html]))

(def template-defaults
  {:name     ""
   :content  ""
   :location })

(defn create-template [attributes]
  (merge template-defaults attributes))

(defn- get-files []
  (let [dir (File. "templates")
        children (.listFiles di)]
    (filter #(.isFile %) children)))

(html/deftemplate template

