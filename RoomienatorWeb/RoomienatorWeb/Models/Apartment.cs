using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace RoomienatorWeb.Models
{
    public class Apartment
    {
        public int Id { get; set; }
        [Required]
        public string Name { get; set; }
        [Required]
        public string Pass { get; set; }

        public virtual ICollection<User> Users { get; set; }
        public virtual ICollection<GroceryItem> GroceryItems { get; set; }
    }
}