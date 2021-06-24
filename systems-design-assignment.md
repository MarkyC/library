# Summary
The university has decided to invest in a revamp of their library's digital service, moving it from a legacy system that's hosted on premise, to a modern, cloud-native system that's built atop a Platform as a Service like AWS.
Create a searchable system for storing the library's catalogue, that library members can use to find and check out books.
# Requirements
## Must Have
* **Store the library's catalogue of books**
    * The catalogue contains 10 million unique books, with one or more copies of each one available
    * Each year, ~700,000 unique books are added to the library's catalogue, and ~300,000 are removed
* **Search books by title, author, and ISBN number**
    * The library's catalogue contains books in >300 languages
    * The library's catalogue is internationally available, and is searched ~275,000 times per day
* **Checkout books that are available**
    * Members are limited to having 10 books checked out at a time
    * Members should receive a notification of their checkout
## Should Have
* **Be notified when your book is overdue**
    * Books can only be checked out for 14 continuous days from the date of checkout
* **Browse books by author and category**
    * Categories are created by the library's staff, and books assigned to them
## Could Have
* **Favourite books that are of interest to you**
    * Members should receive a weekly summary of their top 5 favourite books, ranked by most frequently checked out
