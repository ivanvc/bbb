(ns bbb.template
  (:import java.io.File)
  (:require [net.cgrand.enlive-html :as html]))

(def template-defaults
  {:name     ""
   :filename ""
   :content  ""})

(defn create-template [attributes]
  (merge template-defaults attributes))

(defn parse-template [^File template]
  (create-template {:filename (.getName template)
                    :name     (second (re-matches #"^_([\w-]+)\.html?$" (.getName template)))
                    :content  (html/html-resource template)}))

(defn- get-files []
  (let [dir (File. "templates")
        children (.listFiles dir)]
    (filter #(and (.isFile %)
                  (re-matches #"^_[\w-]+\.html?$" (.getName %))) children)))

(defn parse-templates []
  (let [files (get-files)]
    (map #(parse-template %) files)))

(defn apply-template [post]
  (let [template (first (parse-templates))
        content (template :content)
        content (html/at content [:.title] (html/content (post :title)))
        content (html/at content [:.body]  (html/html-content (post :body)))
        content (html/at content [:.year]  (html/content (post :year)))
        content (html/at content [:.month] (html/content (post :month)))
        content (html/at content [:.day]   (html/content (post :day)))]
    content))

(defn export-template [template posts]
  (let [content       (template :content)
        post-fragment (html/select content [:.wrecker-posts :.wrecker-post])
        post-nodes    (map #(html/at
                              (html/at post-fragment [:a] (html/set-attr :href (:permalink %)))
                              [:.title] (html/content (:title %))) posts)]
    (html/at content [:.wrecker-posts] (html/content post-nodes))))

