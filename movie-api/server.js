const express = require('express');
const app = express();
const PORT = 3000;

app.use(express.json());

let movies = [
  { id: 1, title: 'Batman Begins', year: 2005, "imageUrl": "https://es.web.img2.acsta.net/pictures/14/03/05/11/10/247578.jpg" },
  { id: 2, title: 'The Dark Knight', year: 2008, "imageUrl": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqe2kpVOpsgzMtBsQ2wmfCkScDovKSCMBQOA&s" },
  { id: 3, title: 'The Dark Knight Rises', year: 2012, "imageUrl": "https://i5.walmartimages.com/seo/The-Dark-Knight-Rises-DVD-Warner-Home-Video-Action-Adventure_c3f85ac5-11ef-4b55-9608-89ce32e0a200.9107f8ab0bfdada00f973056234ab16c.jpeg"}
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