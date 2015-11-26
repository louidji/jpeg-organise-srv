Server pour reception et trie des photos
===
API REST pour envoyer des photos (TODO : documenter)
Pour lancer le serveur il est préférable de surchager certaines valeurs par défaut :

    -Dhttp.port=9000 # port
    -Dimages.directory.dest=/data/images # directory de base pour les photos
    -Dimages.directory.temp=/tmp/upload # repertoire temporaire
    -Dlogger.resource=conf/prod-logger.xml # charge du fichier de conf des log de prod

----------
Voir : 

 * https://www.playframework.com/documentation/2.4.x/ProductionConfiguration
 * https://www.playframework.com/documentation/2.4.x/ConfiguringHttps
 
----------
