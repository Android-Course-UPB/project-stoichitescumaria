# Favorite Books App

## Descriere
Aplicatie ce iti permite sa iti salvezi cartile preferate alaturi de informatii despre acestea (note despre carte, cand ai citit cartea etc.). Vei putea cauta cartea citita, vizualiza informatii cum ar fi decriere acesteia si de asemenea vei putea sa o salvezi in cartea in lista ta de carti favorite si sa adaugi note despre aceasta.

## Ecrane
- **Biblioteca generala**: o lista de carti existente si unde utilizatorul va putea cauta cartea dorita
- **Biblioteca de favorite**: lista cu carti salvate la favorite
- **Pagina de detalii a cartii**: informatii precum titlu, autor, descriere, imagine + notele personale despre carte
- **Pagina de salvare a cartii la favorite**: unde se vor putea adauga note personale despre carte.
- **Ecran de setari: schimbarea temei si limba aplicatiei**

## Structura proiectului

- **ui.screens**  
  Contine ecranele principale ale aplicației:
    - `AllBooksScreen` - afișează lista completă de cărți, cu paginare.
    - `FavoriteBooksScreen` - afișează lista cărților favorite.
    - `BookDetailsScreen` - afișează detalii despre o carte selectată.
    - `WelcomeScreen` - ecranul initial, poti selecta daca vrei sa cauti o carte sau sa vezi cele favorite.
    - `SearchBooksScreen` - permite cautarea de carti in biblioteca


- **ui.viewModel**  
  ViewModel-uri care gestionează logica și starea UI:
    - `BooksViewModel` - gestionează lista de cărți și paginarea.
    - `FavoritesViewModel` - gestionează lista de cărți favorite și sincronizarea cu baza de date.

- **data**  
  Componente legate de stocarea locală:
    - `FavoriteBookDao` - DAO Room pentru favoriți.
    - `FavoritesRepository` - interfață între ViewModel și DAO.

- **model**  
  Modelele de date, ex: `BookItem` pentru reprezentarea unei cărți.

- **network**  
  Clasa `BooksApi` care face apelurile REST către un serviciu de căutare cărți (ex: Google Books API).

- **ui.components**  
  Componente UI reutilizabile:
    - `TopBar` - bara de sus cu titlu și butoane.
    - `LoadingScreen` - indicator de încărcare.
    - `ErrorScreen` - ecran de eroare.

- **ui.navigation**  
  Obiecte ce definesc rutele de navigare între ecrane.

---

## Funcționalități principale

- **Căutare cărți**  
  Utilizatorul poate căuta cărți după titlu și autor, cu paginare în lista de rezultate.  
  Apelurile API sunt gestionate asincron în `BooksViewModel`, iar UI-ul reacționează la starea încărcării/succesului/erorii.

- **Gestionarea cărților favorite**  
  Favoritele sunt salvate local în baza de date Room prin `FavoriteBookDao`.  
  `FavoritesViewModel` sincronizează lista favorite și permite adăugarea/eliminarea din favorite.

- **Vizualizarea detaliilor unei cărți**  
  La selectarea unei cărți, utilizatorul vede detalii complete încărcate din API-ul de cărți, cu posibilitatea de a adăuga/elimina cartea din favorite.

- **Tema aplicației**  
  Permite toggle între tema deschisă și tema închisă.

---

## Arhitectură

- Aplicația urmează modelul MVVM:
    - **Model**: datele și API-ul.
    - **ViewModel**: logica de business, gestionarea stărilor UI.
    - **View (Compose UI)**: afișarea și reacția la starea ViewModel-ului.

- Navigarea este gestionată prin Jetpack Compose Navigation, cu rute și parametri pentru detalii.

- Datele locale (favorite) sunt persistate cu Room.

---

## Fluxul aplicației

1. Utilizatorul accesează lista de cărți (AllBooksScreen).
2. Cărțile sunt încărcate din API, cu paginare.
3. Utilizatorul poate marca cărți ca favorite.
4. Lista favorite poate fi accesată în FavoriteBooksScreen, unde se încarcă detaliat fiecare carte favorită.
5. Selectând o carte se ajunge în BookDetailsScreen, unde se afișează informații complete și opțiuni pentru favorite.

---

## Observații

- Starea încărcării, succesului și erorii este gestionată cu tipuri sealed (ex: `BooksUiState`, `FavoritesUiState`), ceea ce ajută la un UI reactiv și clar.
- Aplicația folosește `LaunchedEffect` și `remember` pentru a gestiona efectele secundare și starea locală în Compose.
- Imaginile sunt încărcate asincron cu Coil (`AsyncImage`).