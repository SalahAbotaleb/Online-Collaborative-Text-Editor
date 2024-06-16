# Online Collaborative Text Editor

## Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [Tech Stack](#tech-stack)
4. [How to Run](#how-to-run)
5. [Screenshots](#screenshots)
6. [Demo](#demo)
7. [Algorithm](#algorithm)
8. [Contributors](#contributors)
9. [References](#references)

## Introduction
We have designed and implemented an online real-time collaborative text editor. This type of software enables multiple users on different machines to edit the same document simultaneously, similar to popular tools like Google Docs.

## Features

### User Management
- **User Registration** and **Authentication**: Register and login to user accounts.

### Document Management
- **File Management**: Create, open, rename, and delete files.
- **Access Control**: Share documents with permissions (viewer or editor); ensure security with owner-only file deletion.
- **List Documents**: View owned and shared documents.

### Real-time Collaborative Editing
- **Support File Editing**: Edit document text with bold and italic formatting.
- **Support Concurrent Edits**: Enable multiple users to edit simultaneously; manage conflicts effectively.
- **Real-time Updates**: Track edits in real-time; see other users' cursors and active sessions.

### UI
- **Simple UI**: Includes login, sign up, intuitive file management (create, list, delete, rename, share, open), and text editing capabilities.

## Tech Stack
- **Backend:** 
    - Java
    - Spring Boot
    - Spring Security
    - STOMP Web Sockets
    - SQL Database
- **Frontend:** 
    - React.js
    - Quilljs

## How to Run
1. Clone the repository using:
    ```sh
    git clone https://github.com/SalahAbotaleb/Online-Collaborative-Text-Editor.git
    ```
2. Redirect to the backend folder:
    ```sh
    cd Online-Collaborative-Text-Editor/backend
    ```
3. Build the `gradle.build` file:
    ```sh
    ./gradlew build
    ```
4. Run the Spring Boot app on localhost:
    ```sh
    ./gradlew bootRun
    ```
5. Redirect to the frontend folder:
    ```sh
    cd ../frontend
    ```
6. Install the dependencies:
    ```sh
    npm i
    ```
7. Run the development server:
    ```sh
    npm run dev
    ```
8. Open a web browser and go to the host link.

## Screenshots
#### Login view
![](/Images/Login.PNG)
#### Sign up view
![](/Images/Signup.PNG)
#### Homepage
![](/Images/Homepage.PNG)
#### Edit Page
![](/Images/EditPage.PNG)
#### Share with other users
![](/Images/ShareWithOthers.PNG)
#### Collaborative Editing
![](/Images/OtherUserView.png)
#### Realtime Viewing For Active Users
![](/Images/ViewActiveUsers.PNG)
## Demo

https://github.com/SalahAbotaleb/Online-Collaborative-Text-Editor/assets/95881664/0874a228-a355-4fb2-9463-f151780375a0

## Algorithm
The general idea of the algorithm is very similar to a doubly linked list. Each node contains:
- Node ID
- ID of the left node
- ID of the right node
- Character
- Bold
- Italic
- Deleted

The ID of any node consists of the operation number of the user, "@" and the username. For example, if user Moaaz adds the letter 'b' as his fourth operation, the ID of the node is "3@moaaz".

Inserting is done using the left and right IDs as if they are pointers (since there are no pointers in JS, we use a hashmap that contains the node with access by ID of the element).

We use the username as a tiebreaker. Lexicographically higher numbers mean higher priority. The higher priority nodes are placed to the left.

- For delete, the operation is simply to set the item as deleted.
- For showing an item, we search for the first item to its left that was not deleted.
- For italic and bold, in normal insertion items are set as bold and italic.
- For selecting multiple elements then bolding or italicizing, the loop goes through all the IDs and sets the `isBold` or `isItalic` or both.
- Conflicts are resolved using CRDT data structure.

## Contributors
* [Omar Elzahar](https://github.com/omarelzahar02)
* [Salah Abotaleb](https://github.com/SalahAbotaleb)
* [Hussien Elhawary](https://github.com/Hussein-Elhawary)
* [Moaaz Tarek](https://github.com/moa234)

## References
- [Course materials from CMPS211 Advanced Programming Techniques, Computer Engineering Cairo University](./Project_Description.pdf)
- [Conflict-free Replicated Data Type Research Paper](./CRDT%20Paper%20Research.pdf)
