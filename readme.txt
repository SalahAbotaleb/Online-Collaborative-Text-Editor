1- clone repository using 
git clone https://SalahAbotaleb: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@github.com/SalahAbotaleb/Online-Collaborative-Text-Editor.git
2- redirect to the backend folder
3- build gradle.build file
4- then run the spring boot app on local host
5- redirect to the frontend folder
6- run npm i
7- run npm run dev
8- open web browser to the link provided after running step 7


Extras:
cursor feature that appear at the client side where the other editors are typing or standing at

Algorithm
the general idea of the Algorithm is very similar to a doubly linked list
each node contains an node id, id of the left node, id to the right node, character, bold,italic,deleted
the id of any node consists of the operation number of the user , "@" and the user name 
ex. if user moaaz adds the letter b as his fourth operation the id of the node is "3@moaaz"
inserting is done used the left and right ids as if they are a pointers 
(since there are no pointers in js we used a hashmap that contains the node with access by id of element)
we use the username as a tie breaker lexicographicly higher number means higher priority 
the higher priority nodes are placed to the left
for delete the operation is simply to set the item as deleted
for showing an item we search for the first item to its left that was not deleted
for italic and bold in normal insertion items are set as bold and italic 
for selectiong multiple elements then bolding or italic loops on all the ids and
sets the isbold or isitalic or both 