SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Album](
	[AlbumID] [int] IDENTITY(1,1) NOT NULL,
	[AlbumInfo] [int] NULL,
	[Music] [int] NULL,
 CONSTRAINT [PK__tmp_ms_x__97B4BE174695C631] PRIMARY KEY CLUSTERED 
(
	[AlbumID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO


