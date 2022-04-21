# fetch
Downloads webpages and associated images from the given url and stores it in the current folder for offline viewing.

1. Download webpages and their assets by using the program as follows. Any number or urls can be used.

`java fetch https://www.reddit.com www.goodreads.com sdfdfs www.github.com`

<img width="745" alt="download webpages" src="https://user-images.githubusercontent.com/18500746/164554008-a90c5578-67f2-4e6e-85a8-b5458c4c5222.png">

<img width="1397" alt="download webpages - result" src="https://user-images.githubusercontent.com/18500746/164554025-7fdd0b76-a69e-4d39-8ed9-6cc2799f8437.png">


2. View the metadata of the given urls and the last time they were fetched. It they were not previously downloaded then the webpages will be downloaded too.

`java fetch —metadata https://www.reddit.com www.goodreads.com sdfdfs www.github.com`

<img width="1006" alt="fetch metadata" src="https://user-images.githubusercontent.com/18500746/164554055-5cb5f5f6-092d-4dfa-9b67-532445ade737.png">


Program can be built using docker. 
