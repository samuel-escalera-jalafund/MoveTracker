const express = require('express');
const app = express();
const PORT = 3000;

app.use(express.json());

let movies = [
  { id: 1, title: 'Batman Begins', year: 2005, "imageUrl": "https://es.web.img2.acsta.net/pictures/14/03/05/11/10/247578.jpg" },
  { id: 2, title: 'The Dark Knight', year: 2008, "imageUrl": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqe2kpVOpsgzMtBsQ2wmfCkScDovKSCMBQOA&s" },
  { id: 3, title: 'The Dark Knight Rises', year: 2012, "imageUrl": "https://i5.walmartimages.com/seo/The-Dark-Knight-Rises-DVD-Warner-Home-Video-Action-Adventure_c3f85ac5-11ef-4b55-9608-89ce32e0a200.9107f8ab0bfdada00f973056234ab16c.jpeg"},
  { id: 4, title: 'Iron Man', year: 2008, "imageUrl": "https://es.web.img3.acsta.net/medias/nmedia/18/66/64/01/20168724.jpg" },
  { id: 5, title: 'Superman', year: 2013, "imageUrl": "https://static.wikia.nocookie.net/dcextendeduniverse/images/3/3b/Man_of_Steel.png/revision/latest?cb=20151123165829&path-prefix=es" },
  { id: 6, title: 'Pratas del caribe', year: 2003, "imageUrl": "https://m.media-amazon.com/images/S/pv-target-images/201f12fbed17f0265c2041d102b8525ee610a2c99923dedb9b0b8f3887fe8c6b.jpg" },
  { id: 7, title: 'El gran showman', year: 2003, "imageUrl": "https://upload.wikimedia.org/wikipedia/en/1/10/The_Greatest_Showman_poster.png" },
];

app.get('/movies', (req, res) => {
  const search = req.query.search?.toLowerCase();
  if (search) {
    return res.json(movies.filter(m => m.title.toLowerCase().includes(search)));
  }
  res.json(movies);
});

app.get('/movies/:id', (req, res) => {
  const movie = movies.find(m => m.id == req.params.id);
  if (movie) res.json(movie);
  else res.status(404).send('Not found');
});

app.post('/movies', (req, res) => {
  const { title, year } = req.body;
  const id = movies.length + 1;
  const newMovie = { id, title, year };
  movies.push(newMovie);
  res.status(201).json(newMovie);
});

app.listen(PORT, () => console.log(`API corriendo en http://localhost:${PORT}`));