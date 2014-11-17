using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Library.Models;

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

        public ActionResult ReturnBooks()
        {

            return View();
        }

        public ActionResult AllBooks()
        {
            ViewBag.Message = "Here are all books listed, you can add any books with a valid ISBN.";
            List<Books> items = null;
            using (var d = new BookDb())
            {
                items=d.Books.AsEnumerable().ToList();
                return View(items);
            }

        }
        [HttpPost]
        public ActionResult AllBooks(Books model)
        {

            using (var d = new BookDb())
            {
                d.Books.Add(model);
                d.SaveChanges();
            }

            return AllBooks();
        }

        public ActionResult RentBooks()
        {
            return View();
        }
    }
}
