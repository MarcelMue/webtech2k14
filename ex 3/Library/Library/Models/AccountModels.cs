using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity;
using System.Globalization;
using System.Web.Security;

namespace Library.Models
{
    //Database context
    public class UserContext : DbContext
    {
        public UserContext()
            : base("MyCon")
        {
        }
        public DbSet<Books> Books { get; set; }
        public DbSet<Rented> Rented { get; set; }
        public DbSet<UserProfile> UserProfiles { get; set; }
        public DbSet<History> History { get; set; }
    }

    //Layout of the Books Table
    [Table("Books")]
    public class Books
    {
        [Key]
        [DatabaseGeneratedAttribute(DatabaseGeneratedOption.Identity)]
        public int BookKey { get; set; }

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

    //Layout of the Rented Table
    [Table("Rented")]
    public class Rented
    {
        [Key]
        [DatabaseGeneratedAttribute(DatabaseGeneratedOption.Identity)]
        public int RentKey { get; set; }
        public bool returned { get; set; }
        public virtual UserProfile UserProfile { get;set; }
        public virtual Books Book { get; set; }
    }

    //Layout of the History Table
    [Table("History")]
    public class History
    {
        [Key]
        [DatabaseGeneratedAttribute(DatabaseGeneratedOption.Identity)]
        public int HistoryId { get; set; }
        public States State { get; set; }
        public DateTime Date { get; set; }
        public virtual UserProfile UserProfile { get; set; }
        public virtual Books Book { get; set; }
    }

    //The next few classes are Models which are used to carry data to views
    public class HistoryModel
    {
        public IEnumerable<History> History { get; set; }
    }

    public class AllBooksModel
    {
        public IEnumerable<Books> AllBooks { get; set; }
        public Books NewBook { get; set; }
    }

    public class RentBooksModel
    {
        public IEnumerable<Books> AllBooks { get; set; }
        public IEnumerable<Rented> AllRented { get; set; }
        public Books NewRented { get; set; }
    }

    public class ReturnBooksModel
    {
        public IEnumerable<Rented> AllRented { get; set; }
        public ReturnModel NewReturned { get; set; }
    }

    public class ReturnModel
    {
        public int UserId { get; set; }
        public int BookKey { get; set; }
    }

    //Enums to make the code prettier
    public enum States
    {
        Rented =1,
        Returned = 2
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
        Educational
    }

    //User related Table & following Models
    [Table("UserProfile")]
    public class UserProfile
    {
        [Key]
        [DatabaseGeneratedAttribute(DatabaseGeneratedOption.Identity)]
        public int UserId { get; set; }
        public string UserName { get; set; }
    }

    public class RegisterExternalLoginModel
    {
        [Required]
        [Display(Name = "User name")]
        [EmailAddress]
        public string UserName { get; set; }
        public string ExternalLoginData { get; set; }
    }

    public class LocalPasswordModel
    {
        [Required]
        [DataType(DataType.Password)]
        [Display(Name = "Current password")]
        public string OldPassword { get; set; }

        [Required]
        [StringLength(100, ErrorMessage = "The {0} must be at least {2} characters long.", MinimumLength = 6)]
        [DataType(DataType.Password)]
        [Display(Name = "New password")]
        public string NewPassword { get; set; }

        [DataType(DataType.Password)]
        [Display(Name = "Confirm new password")]
        [Compare("NewPassword", ErrorMessage = "The new password and confirmation password do not match.")]
        public string ConfirmPassword { get; set; }
    }

    public class LoginModel
    {
        [Required]
        [EmailAddress]
        [Display(Name = "User name")]
        public string UserName { get; set; }

        [Required]
        [DataType(DataType.Password)]
        [Display(Name = "Password")]
        public string Password { get; set; }

        [Display(Name = "Remember me?")]
        public bool RememberMe { get; set; }
    }

    public class RegisterModel
    {
        [Required]
        [Display(Name = "User name")]
        [EmailAddress]
        public string UserName { get; set; }

        [Required]
        [StringLength(100, ErrorMessage = "The {0} must be at least {2} characters long.", MinimumLength = 6)]
        [DataType(DataType.Password)]
        [Display(Name = "Password")]
        public string Password { get; set; }

        [DataType(DataType.Password)]
        [Display(Name = "Confirm password")]
        [Compare("Password", ErrorMessage = "The password and confirmation password do not match.")]
        public string ConfirmPassword { get; set; }
    }

}
