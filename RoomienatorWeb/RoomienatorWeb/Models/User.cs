using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace RoomienatorWeb.Models
{
    public class User
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Pass { get; set; }
        public byte[] Avatar { get; set; }

        public Nullable<int> ApartmentID { get; set; }
        [JsonIgnore]
        public virtual Apartment Apartment { get; set; }
    }
}