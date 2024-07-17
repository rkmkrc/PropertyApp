import { cookies } from 'next/headers';
import { NextRequest, NextResponse } from 'next/server';

export async function DELETE(req: NextRequest, { params }: { params: { id: string } }) {
  const { id } = params;

  if (!id) {
    return NextResponse.json(
      { success: false, message: 'Listing ID is required' },
      { status: 400 }
    );
  }

  const cookieStore = cookies();
  const token = cookieStore.get("token")?.value;

  if (!token) {
    return NextResponse.json(
      { success: false, message: 'Unauthorized' },
      { status: 401 }
    );
  }

  const deleteListingUrl = `http://localhost:8080/api/v1/users/listings/${id}`;

  const response = await fetch(deleteListingUrl, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });

  const data = await response.json();

  if (response.ok) {
    return NextResponse.json({ success: true, message: 'Listing deleted successfully', data: data });
  } else {
    return NextResponse.json(
      { success: false, message: data.message || 'Listing deletion failed' },
      { status: response.status }
    );
  }
}
