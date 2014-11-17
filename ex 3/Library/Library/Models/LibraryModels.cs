using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity;
using System.Globalization;
using System.Web.Mvc;
using System.Web.Security;

namespace Library.Models
{
    public class BookDb : DbContext
    {
        public DbSet<Books> Books { get; set; }
    }
    public class Books
    {
        [Key]
        [DatabaseGeneratedAttribute(DatabaseGeneratedOption.Identity)]
        public int key { get; set; }
        [Required]
        [Display(Name="ISBN (without dashes)")]
        [RegularExpression(@"^(97(8|9))?\d{9}(\d|X)$")]
        public string ISBN { get; set; }
        [Required]
        public string Title { get; set; }
        [Required]
        public string Author { get; set; }
        [Required]
        [Display(Name="Category")]
        public Categories Category { get; set; }
    }

    public enum Categories
    {
        Thriller,
        Horror,
        Adventure,
        Love,
        Fantasy,
        History,
        ScienceFiction,
        Exception
    }
}
