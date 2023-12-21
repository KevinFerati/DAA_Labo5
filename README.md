# Rapport Labo 05 DAA
- Flavio Sovilla
- Kevin Ferati
- Malo Romano

# Questions 3
## 3.1 Veuillez expliquer comment votre solution s’assure qu’une éventuelle Couroutine associée à une vue (item) de la RecyclerView soit correctement stoppée lorsque l’utilisateur scrolle dans la galerie et que la vue est recyclée.

Pour annuler le job d'associée à un item, on associe à chaque ViewHolder le job de récupération de l'image. Au recyclage, on annule le job en question.


## 3.2 Comment pouvons-nous nous assurer que toutes les Coroutines soient correctement stoppées lorsque l’utilisateur quitte l’Activité ? Veuillez expliquer la solution que vous avez mise en oeuvre, est-ce la plus adaptée ?

Toutes les tâches que nous avons créé sont exécutées dans le scope `lifecycleScope`, associée à l'activité `MainActivity`. Ainsi, les tâches sont annulées automatiquement à la destruction de l'activité. C'est la plus adaptée car la plus simple et c'est géré par le système.

## 3.3 Est-ce que l’utilisation du Dispatchers.IO est la plus adaptée pour des tâches de téléchargement ? Ou faut-il plutôt utiliser un autre Dispatcher, si oui lequel ? Veuillez illustrer votre réponse en effectuant quelques tests.

Oui, car la majorité du travail consiste en de l'attente. Si on change par Default, le nombre de tâches limitée par le CPU. Main ne fonctionne pas car les opérations bloquantes sont interdites dans l'UI-Thread.

Dans le cas de Default, on remarque notamment bien plus de lenteurs dans le cas où on scroll loin.

## 3.4 Nous souhaitons que l’utilisateur puisse cliquer sur une des images de la galerie afin de pouvoir, par exemple, l’ouvrir en plein écran. Comment peut-on mettre en place cette fonctionnalité avec une RecyclerView? Comment faire en sorte que l’utilisateur obtienne un feedback visuel lui indiquant que son clic a bien été effectué, sur la bonne vue.

1.Dans le constructeur du ViewHolder, pour la vue qui a été passée. ajouter un listener à la vue passée en paramètre
2. Dans le callback, faire afficher l'imgView associée au viewHolder en plein écran

# Questions 4
## 4.1 Lors du lancement de la tâche ponctuelle, comment pouvons-nous faire en sorte que la galerie soit rafraîchie ?
En appellant ```galleryAdapter.notifyDataSetChanged()```. L'adapter de la liste est averti que le dataset a été modifié.


## 4.2 Comment pouvons-nous nous assurer que la tâche périodique ne soit pas enregistrée plusieurs fois ? Vous expliquerez comment la librairie WorkManager procède pour enregistrer les différentes tâches périodiques et en particulier comment celles-ci sont ré-enregistrées lorsque le téléphone est redémarré.
Le WorkManager a sa propre db SQLite, dans laquelle elle stocke les dernières exécutions. Elle peut donc les réinscrire lorsque le téléphone redémarre.
