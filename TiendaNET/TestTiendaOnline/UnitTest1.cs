using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Moq;
using TiendaOnline.Controllers;
using TiendaOnline.Data;
using TiendaOnline.Models;
using Moq.EntityFrameworkCore;
using Humanizer.Localisation;

namespace TestTiendaOnline
{
    public class UnitTest1
    {
        [Fact]
        public async Task IndexTest()
        {
            // Arrange: Crear una lista de películas de prueba
            // Arrange: Crear una lista de películas de prueba
            var movies = new List<Movie>
        {
            new Movie { Id = 1, Title = "Action Movie", Genre = "Action" },
            new Movie { Id = 2, Title = "Comedy Movie", Genre = "Comedy" }
        };

            // Crear un mock del contexto con Moq.EntityFrameworkCore
            var mockContext = new Mock<MvcMovieContext>();
            mockContext.Setup(c => c.Movie).ReturnsDbSet(movies);

            // Crear el controlador usando el contexto simulado
            var controller = new MoviesController(mockContext.Object);

            // Actuar: Llamar al método Index con los parámetros de filtro
            var result = await controller.Index("Action", "Action") as ViewResult;

            // Aserción
            var model = result?.Model as MovieGenreViewModel;
            Assert.NotNull(model);
            Assert.Single(model.Movies);
            Assert.Equal("Action Movie", model.Movies[0].Title);
        }

        [Fact]
        public async Task DetailsMoviesTest()
        {
            // Arrange: Crear una lista de películas de prueba
            // Arrange: Crear una lista de películas de prueba
            var movies = new List<Movie>
        {
            new Movie { Id = 1, Title = "Action Movie", Genre = "Action" },
            new Movie { Id = 2, Title = "Comedy Movie", Genre = "Comedy" }
        };

            // Crear un mock del contexto con Moq.EntityFrameworkCore
            var mockContext = new Mock<MvcMovieContext>();
            mockContext.Setup(c => c.Movie).ReturnsDbSet(movies);

            // Crear el controlador usando el contexto simulado
            var controller = new MoviesController(mockContext.Object);

            // Actuar: Llamar al método Index con los parámetros de filtro
            var result = await controller.Details(1) as ViewResult;

            // Aserción
            var model = result?.Model as Movie;
            Assert.NotNull(model);
            Assert.Equal("Action Movie", model.Title);
            Assert.Equal("Action", model.Genre);
        }

        [Fact]
        public async Task EditMoviesTest()
        {
            // Arrange
            var mockContext = new Mock<MvcMovieContext>();
            var mockDbSet = new Mock<DbSet<Movie>>();

            var movieToEdit = new Movie { Id = 1, Title = "Original Title", Genre = "Action" };

            mockDbSet.Setup(m => m.FindAsync(1)).ReturnsAsync(movieToEdit);
            mockContext.Setup(c => c.Movie).Returns(mockDbSet.Object);

            var controller = new MoviesController(mockContext.Object);

            var updatedMovie = new Movie { Id = 1, Title = "Updated Title", Genre = "Action" };

            // Act
            var result = await controller.Edit(1, updatedMovie);

            // Assert
            mockContext.Verify(c => c.Update(It.IsAny<Movie>()), Times.Once);
            mockContext.Verify(c => c.SaveChangesAsync(default), Times.Once);

            var redirectToActionResult = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirectToActionResult.ActionName);
            Assert.Equal("Updated Title", updatedMovie.Title);
        }

        [Fact]
        public async Task DeleteMoviesTest()
        {
            var movies = new List<Movie>
    {
        new Movie { Id = 1, Title = "Test Movie to Delete", Genre = "Drama" }
    };

            // Arrange
            var mockContext = new Mock<MvcMovieContext>();
            var mockDbSet = new Mock<DbSet<Movie>>();

            var movie = new Movie { Id = 1, Title = "Test Movie", Genre = "Genre" };
            mockDbSet.Setup(m => m.FindAsync(1)).ReturnsAsync(movie);
            mockDbSet.Setup(m => m.Remove(It.IsAny<Movie>())).Verifiable();

            mockContext.Setup(c => c.Movie).Returns(mockDbSet.Object);

            var controller = new MoviesController(mockContext.Object);

            // Act
            var result = await controller.DeleteConfirmed(1);

            // Assert
            mockDbSet.Verify(m => m.Remove(It.Is<Movie>(mov => mov == movie)), Times.Once);
            mockContext.Verify(c => c.SaveChangesAsync(default), Times.Once);

            var redirectToActionResult = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirectToActionResult.ActionName);
        }

    }
}