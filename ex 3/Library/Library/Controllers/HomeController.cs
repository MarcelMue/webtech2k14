using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Security;
using Library.Models;
using Library.Filters;
using WebMatrix.WebData;

namespace Library.Controllers
{
    [Authorize]
    public class HomeController : Controller
    {
        [AllowAnonymous]
        public ActionResult Index()
        {
            ViewBag.Message = "Modify this template to jump-start your ASP.NET MVC application.";

            return View();
        }

        public ActionResult AllBooks()
        {
            ViewBag.Message = "Here are all books listed, you can add any books with a valid ISBN.";
            List<Books> items = null;
            using (var d = new UserContext())
            {
                items=d.Books.AsEnumerable().ToList();
                AllBooksModel model = new AllBooksModel();
                model.AllBooks = items;
                return View(model);
            }

        }
        [HttpPost]
        public ActionResult AllBooks(AllBooksModel model)
        {

            using (var d = new UserContext())
            {
                d.Books.Add(model.NewBook);
                d.SaveChanges();
            }

            return AllBooks();
        }

        public ActionResult RentBooks()
        {
            List<Rented> rented = null;
            List<Books> books = null;
            using (var d = new UserContext())
            {
                rented = d.Rented.AsEnumerable().ToList();
                books = d.Books.AsEnumerable().ToList();
                RentBooksModel model = new RentBooksModel();
                model.AllRented = rented;
                model.AllBooks = books;
                return View(model);
            }
        }

        [HttpPost]
        [InitializeSimpleMembership]
        public ActionResult RentBooks(RentBooksModel model)
        {
            Rented NewRented = new Rented();
            using (var d = new UserContext())
            {
                NewRented.UserProfile = d.UserProfiles.Find((int)WebSecurity.CurrentUserId);
                NewRented.Book = d.Books.Find(model.NewRented.BookKey);
                NewRented.returned = true;
                Rented comp = d.Rented.Where(x => x.UserProfile.UserId == (int)WebSecurity.CurrentUserId)
                    .Where(x => x.Book.BookKey == model.NewRented.BookKey)
                    .FirstOrDefault();
                if (comp == null)
                {
                    NewRented.returned = false;
                    d.Rented.Add(NewRented);
                }
                else
                {
                    comp.returned = false;
                }
                d.SaveChanges();
            }
            return RentBooks();
        }
        
        public ActionResult ReturnBooks()
        {
            List<Rented> rented = null;
            using (var d = new UserContext())
            {
                rented = d.Rented.Include("Book").Include("UserProfile").AsEnumerable().ToList();
                ReturnBooksModel model = new ReturnBooksModel();
                model.AllRented = rented;
                model.NewReturned = new ReturnModel();
                return View(model);
            }
            
        }

        [HttpPost]
        [InitializeSimpleMembership]
        public ActionResult ReturnBooks(ReturnBooksModel model)
        {
            using (var d = new UserContext())
            {
                Rented r = d.Rented.Where(x => x.UserProfile.UserId == model.NewReturned.UserId)
                    .Where(x => x.Book.BookKey == model.NewReturned.BookKey)
                    .FirstOrDefault();
                r.returned = true;
                d.SaveChanges();
                return ReturnBooks();
            }
        }

    }
}
