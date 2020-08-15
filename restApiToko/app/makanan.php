<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class makanan extends Model
{
    public function pesananDetail(){
        return $this->belongsTo('App\pesananDetail','makanan_id','makanan_id');
    }
}
