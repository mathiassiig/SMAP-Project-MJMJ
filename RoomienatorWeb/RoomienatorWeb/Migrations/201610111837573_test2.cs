namespace RoomienatorWeb.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class test2 : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Apartments",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(nullable: false),
                        Pass = c.String(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.GroceryItems",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(nullable: false),
                        Price = c.Int(nullable: false),
                        Creation = c.DateTime(nullable: false),
                        Bought = c.DateTime(nullable: false),
                        UserId = c.Int(nullable: false),
                        ApartmentID = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Apartments", t => t.ApartmentID, cascadeDelete: true)
                .ForeignKey("dbo.Users", t => t.UserId, cascadeDelete: true)
                .Index(t => t.UserId)
                .Index(t => t.ApartmentID);
            
            CreateTable(
                "dbo.Users",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(),
                        Pass = c.String(),
                        Avatar = c.Binary(),
                        ApartmentID = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Apartments", t => t.ApartmentID)
                .Index(t => t.ApartmentID);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.GroceryItems", "UserId", "dbo.Users");
            DropForeignKey("dbo.Users", "ApartmentID", "dbo.Apartments");
            DropForeignKey("dbo.GroceryItems", "ApartmentID", "dbo.Apartments");
            DropIndex("dbo.Users", new[] { "ApartmentID" });
            DropIndex("dbo.GroceryItems", new[] { "ApartmentID" });
            DropIndex("dbo.GroceryItems", new[] { "UserId" });
            DropTable("dbo.Users");
            DropTable("dbo.GroceryItems");
            DropTable("dbo.Apartments");
        }
    }
}
